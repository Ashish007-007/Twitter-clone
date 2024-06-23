package tech.codingclub.helix.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.codingclub.helix.database.GenericDB;
import tech.codingclub.helix.entity.*;
import tech.codingclub.helix.global.SysProperties;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: rishabh
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/welcome")
    public String userWelcome(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        Member member=ControllerUtils.getCurrentMember(request);
        //access only when he is logined


        return "welcome";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/public-tweet/{id}")
    public
    @ResponseBody
    List<TweetUI> fetchTweet(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        Condition condition=tech.codingclub.helix.tables.Tweet.TWEET.ID.lessThan(id);
        List<Tweet> data= (List<Tweet>) GenericDB.getRows(tech.codingclub.helix.tables.Tweet.TWEET, Tweet.class,condition,3,tech.codingclub.helix.tables.Tweet.TWEET.ID.desc());
        Set<Long> memberIds=new HashSet<Long>();
        for(Tweet tweet:data){
            memberIds.add(tweet.author_id);
        }
        HashMap<Long,Member> memberHashMap=new HashMap<Long, Member>();
        Condition memberCondition= tech.codingclub.helix.tables.Member.MEMBER.ID.in(memberIds);
        List<Member> members= (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class,memberCondition,null);
        for(Member member:members){
            memberHashMap.put(member.id,member);

        }
        ArrayList<TweetUI> tweetUIS=new ArrayList<TweetUI>();
        for(Tweet tweet:data){
            Member member=memberHashMap.get(tweet.author_id);
            TweetUI tweetUI=new TweetUI(tweet,member);
            tweetUIS.add(tweetUI);

        }
        return tweetUIS;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-post")
    public
    @ResponseBody
    String createTweet(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        Tweet t = new Tweet(data, null, new Date().getTime(), ControllerUtils.getUserId(request));
        new GenericDB<Tweet>().addRow(tech.codingclub.helix.tables.Tweet.TWEET, t);
        return "Posted Successfully";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/follow-member")
    public
    @ResponseBody
    String followMember(@RequestBody Long memberId, HttpServletRequest request, HttpServletResponse response) {
        Long currentUserId = ControllerUtils.getUserId(request);
        if (currentUserId != null && memberId != null && !currentUserId.equals(memberId)) {
            Follower follower = new Follower(currentUserId, memberId);
            new GenericDB<Follower>().addRow(tech.codingclub.helix.tables.Follower.FOLLOWER, follower);
            return "Connected Successfully";
        } else {
            return "Not permitted !";
        }


    }

    @RequestMapping(method = RequestMethod.POST, value = "/un-follow-member")
    public
    @ResponseBody
    String unfollowMember(@RequestBody Long memberId, HttpServletRequest request, HttpServletResponse response) {
        Long currentUserId = ControllerUtils.getUserId(request);
        if (currentUserId != null && memberId != null && !currentUserId.equals(memberId)) {
            Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(currentUserId).and(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID.eq(memberId));
            boolean success = GenericDB.deleteRow(tech.codingclub.helix.tables.Follower.FOLLOWER, condition);
            if (success) {
                return "Un followed Successfully";
            } else {
                return "Not permitted !";
            }
        }
        return "Backend error";

    }
    private void preLoadVariables(ModelMap modelMap,Long currentMemberId){
        modelMap.addAttribute("USER_IMAGE","/images/profile-image/"+currentMemberId+".jpeg");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/recommendations")
    public String welcome(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        Member member = ControllerUtils.getCurrentMember(request);
        List<Member> members = (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER, Member.class, null, 10, tech.codingclub.helix.tables.Member.MEMBER.ID.desc());
        //access only when he is logined

        //members data
        ArrayList<Long> memberIds = new ArrayList<Long>();
        for (Member m : members) {
            memberIds.add(m.id);
        }
        Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(member.id).and(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID.in(memberIds));
        List<Follower> followerRows = (List<Follower>) GenericDB.getRows(tech.codingclub.helix.tables.Follower.FOLLOWER, Follower.class, condition, null);

        Set<Long> followedMemberIds = new HashSet<Long>();
        for (Follower follower : followerRows) {
            followedMemberIds.add(follower.following_id);
        }
        for (Member memberTemp : members) {
            if (followedMemberIds.contains(memberTemp.id)) {
                memberTemp.is_followed = true;
            }
        }

        modelMap.addAttribute("NAME", member.name);
        preLoadVariables(modelMap,member.id);
        modelMap.addAttribute("RECOMMENDATIONS", members);
        return "recommendations";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/followed")
    public String followed(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        Long currentMemberId = ControllerUtils.getUserId(request);
        Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(currentMemberId);

        List<Long> followedIds = new GenericDB<Long>().getColumnRows(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID, tech.codingclub.helix.tables.Follower.FOLLOWER, Long.class, condition, 100);

        Condition selectMemberCondition = tech.codingclub.helix.tables.Member.MEMBER.ID.in(followedIds);
        List<Member> followedMembers = (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER, Member.class, selectMemberCondition, 10, tech.codingclub.helix.tables.Member.MEMBER.ID.desc());
        //access only when he is logined

        preLoadVariables(modelMap,currentMemberId);
        modelMap.addAttribute("FOLLOWED", followedMembers);
        return "followed";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update")
    public String updateUser(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        return "updateuser";
    }

    private static String saveUploadedFile( MultipartFile file, Long userId){
        try {
            String path = SysProperties.getBaseDir()+"/images/profile-image/"+userId+".jpeg";
            file.transferTo( new File(path));
            return "/images/profile-image/"+userId+".jpeg";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/profile-image/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile, HttpServletRequest request) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        String path = "";
        try {
            Long currentMemberId = ControllerUtils.getUserId(request);
            path = saveUploadedFile(uploadfile,currentMemberId);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(path, new HttpHeaders(), HttpStatus.OK);
    }
}
package com.easychat.sse.controller;

import com.easychat.sse.aspect.ValidAuth;
import com.easychat.sse.model.dto.*;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.model.vo.SimpleGroupVO;
import com.easychat.sse.model.vo.UserInfoVO;
import com.easychat.sse.response.R;
import com.easychat.sse.service.UserService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.easychat.sse.shiro.ShiroUtil.getUser;
import static com.easychat.sse.shiro.ShiroUtil.getUserId;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/register")
    public R<String> doRegister(@RequestParam String account,
                                @RequestParam String nickName,
                                @RequestParam String password) {
        userService.doRegister(account, nickName, password);
        return R.success("注册成功");
    }

    @GetMapping("/checkRepeat")
    public R<String> checkRepeat(@RequestParam String account) {
        if (ObjectUtils.isEmpty(account)) {
            return R.fail("用户名不能为空");
        }
        UserEntity user = userService.getUserByAccount(account);
        if (user != null) {
            return R.fail("账户名已存在");
        }
        return R.success();
    }

    @GetMapping("/groups")
    public R<List<IdTitle>> myGroups() {
        return R.success(userService.getUserGroups(getUserId()));
    }

    @GetMapping("/searchPage/{action}")
    public ModelAndView searchPage(@PathVariable String action) {
        return new ModelAndView("chat/searchUser");
    }

    @GetMapping("/search")
    public R<List<SimpleUser>> search(@RequestParam String content) {
        if (ObjectUtils.isEmpty(content)) {
            return R.success(Collections.emptyList());
        }
        List<SimpleUser> result = userService.searchUser(content);
        return R.success(result);
    }

    @GetMapping("/applyPage/{id}")
    public ModelAndView applyPage(@PathVariable String id) {
        SimpleUser simpleUser = userService.getSimpleUserInfo(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", simpleUser);
        return new ModelAndView("chat/applyPage", map);
    }

    @PostMapping("/apply")
    public R<Boolean> apply(@Valid @RequestBody ApplyFriendArgs applyFriendArgs) {
        userService.applyFriend(getUser(), applyFriendArgs);
        return R.success();
    }


    @GetMapping("/group-list")
    @ValidAuth(hasRole = 1)
    public R<List<IdName>> groupList() {
        return R.success(userService.queryUserGroup(getUserId()));
    }

    /**
     * groupId可传可不传
     *
     * @param id groupId
     */
    @GetMapping("/friends")
    public R<List<SimpleGroupVO>> myFriends(@RequestParam(required = false) String id) {
        List<SimpleGroupVO> friends = userService.findMyFriends(getUserId(), id);
        return R.success(friends);
    }

    @GetMapping("/infoPage")
    public ModelAndView infoPage() {
        return new ModelAndView("chat/userInfo");
    }

    @GetMapping("/info-edit")
    public ModelAndView infoEdit() {
        return new ModelAndView("chat/editUserInfo");
    }

    @GetMapping("/info")
    public R<UserInfoVO> userInfo() {
        UserInfoVO userInfoVO = userService.getUserInfo(getUserId());
        return R.success(userInfoVO);
    }

}

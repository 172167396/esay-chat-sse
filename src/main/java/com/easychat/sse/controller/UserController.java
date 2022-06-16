package com.easychat.sse.controller;

import com.easychat.sse.model.dto.ApplyFriendArgs;
import com.easychat.sse.model.dto.IdName;
import com.easychat.sse.model.dto.IdTitle;
import com.easychat.sse.model.dto.SimpleUser;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.response.R;
import com.easychat.sse.service.UserService;
import org.springframework.ui.Model;
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
    public R<Boolean> apply(@Valid ApplyFriendArgs applyFriendArgs) {
        userService.applyFriend(getUser(), applyFriendArgs);
        return R.success();
    }


    @GetMapping("/group-list")
    public List<IdName> groupList() {
        return userService.queryUserGroup(getUserId());
    }
}

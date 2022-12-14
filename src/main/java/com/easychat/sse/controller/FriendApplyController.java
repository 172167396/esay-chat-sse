package com.easychat.sse.controller;

import com.easychat.sse.model.dto.ApplyAgreeDTO;
import com.easychat.sse.model.vo.FriendApplyVO;
import com.easychat.sse.response.R;
import com.easychat.sse.service.ApplyFriendService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static com.easychat.sse.shiro.ShiroUtil.getUserId;

@RestController
@RequestMapping("/friend")
public class FriendApplyController {

    @Resource
    ApplyFriendService applyFriendService;

    @GetMapping("/received")
    public R<List<FriendApplyVO>> received() {
        List<FriendApplyVO> applies = applyFriendService.queryOwnerReceived(getUserId());
        return R.success(applies);
    }

    @PostMapping("/agree")
    public R<Boolean> received(@RequestBody @Valid ApplyAgreeDTO args) {
        applyFriendService.agree(getUserId(), args);
        return R.success();
    }

    @PostMapping("/ignore")
    public R<Boolean> ignore(String id) {
        applyFriendService.ignore(getUserId(), id);
        return R.success();
    }

    @GetMapping("/applyInfo/{id}")
    public ModelAndView applyInfo(@PathVariable String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        return new ModelAndView("chat/agreeInfo", map);
    }
}

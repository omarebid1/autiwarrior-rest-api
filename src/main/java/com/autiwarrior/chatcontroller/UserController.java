package com.autiwarrior.chatcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autiwarrior.chatmodel.ActiveUser;
import com.autiwarrior.chatmodel.Storage;

@RestController
//@CrossOrigin("http://localhost:63342")
@CrossOrigin(origins = "*")
public class UserController {

    @GetMapping("/active")
    public List<ActiveUser> list(){
        return Storage.activeUserList;
    }
}

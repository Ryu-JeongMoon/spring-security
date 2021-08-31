package com.springsecurity.service;


import com.springsecurity.domain.dto.UserDto;
import com.springsecurity.domain.entity.Account;

import java.util.List;

public interface UserService {

  List<Account> getUsers();
  UserDto getUser(Long id);
  void createUser(Account account);
  void deleteUser(Long idx);
}

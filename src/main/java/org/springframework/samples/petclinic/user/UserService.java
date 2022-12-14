/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.user;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void saveUser(User user) throws DataAccessException {
		user.setEnabled(true);
		userRepository.save(user);
		authoritiesService.saveAuthorities(user.getUsername(), "player");
	}
	
	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}
	@Transactional(readOnly = true)
	public Collection<User> findUserByUsername(String nombre) throws DataAccessException {
		return userRepository.findUserByUsername(nombre);
	}

	public List<User> getUsuarios() {
		return userRepository.findAll();
	}

	public void deleteUserById(String username){
        userRepository.deleteById(username);
    }
/* 
	public User getById(int id){
        return userRepository.findById(id).get();
    }

    public void deleteUserById(int id){
        userRepository.deleteById(id);
    }

    public void save(User user){
        userRepository.save(user);
    }

	*/
	public Optional<User> findUserOptional(String username) {
		return userRepository.findByUsernameOptional(username);
	}

	public List<User> getFriends(String username) {
		return userRepository.getFriendsOf(username);
	}

	public void Deletefriend(String username, String username2){
		userRepository.Deletefriend(username, username2);
		userRepository.Deletefriend(username2, username);
	}
}

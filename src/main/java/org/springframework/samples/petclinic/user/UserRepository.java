package org.springframework.samples.petclinic.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends  CrudRepository<User, String>{

    List<User> findAll();

    @Query("SELECT user FROM User user WHERE user.username LIKE :username")
	public Optional<User> findByUsername(@Param("username") String username);
	
    @Query("SELECT DISTINCT user FROM User user WHERE user.username LIKE :username%")
	public Collection<User> findUserByUsername(@Param("username") String username);

    @Query("SELECT user FROM User user JOIN user.friends f WHERE f.username = :username")
    List<User> getFriendsOf(@Param("username") String username);

    @Modifying
    @Query(value = "DELETE FROM FRIENDS WHERE FRIENDS.FRIEND_ID = ?2 AND FRIENDS.AUX_FRIEND_ID = ?1", nativeQuery = true)
    void Deletefriend(String username, String username2);

    @Query("SELECT user FROM User user WHERE user.username = ?1")
    Optional<User> findByUsernameOptional(String username) throws DataAccessException;
}

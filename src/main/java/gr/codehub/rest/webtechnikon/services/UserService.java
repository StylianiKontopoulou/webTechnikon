package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.models.User;
import java.util.List;

public interface UserService {

    User get(Long id);

    List<User> getUsers();

    User create(User user);

    void delete(Long id);

    void update(User User);
}

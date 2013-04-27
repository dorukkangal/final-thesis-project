package tr.edu.gsu.service.user;

import tr.edu.gsu.domain.user.GoesUser;
import tr.edu.gsu.service.BaseService;

public interface GoesUserService<T extends GoesUser> extends BaseService<T> {

	public T find(String email, String password);
}

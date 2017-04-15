package practice.service.transformer;

import practice.model.User;
import practice.provider.UserDb;
import practice.utils.Utils;

/**
 * UserDb transformer. It generates a {@link User} from a {@link UserDb}.
 */
public class UserDbTransformer implements ITransformer<UserDb, User> {

	@Override
	public User transform(UserDb source) {
		
		User user = null;
		
		if (source != null) {
			user = new User(source.getId(), source.getName(), 
					Utils.stringToLocalDate(source.getBirthdate()));
		}
		
		return user;
	}

}

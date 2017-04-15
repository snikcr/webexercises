package practice.service.transformer;

import org.springframework.validation.annotation.Validated;

import practice.model.User;
import practice.provider.UserDb;
import practice.utils.Utils;

/**
 * User transformer. It generates a {@link UserDb} from a {@link User}.
 */
public class UserTransformer implements ITransformer<User, UserDb> {

	@Override
	public UserDb transform(@Validated User source) {
		
		UserDb userDb = null;
		
		if (source != null) {
			userDb = new UserDb(source.getId(), source.getName(), 
					Utils.localDateToString(source.getBirthdate()));
		}
		
		return userDb;
	}

}

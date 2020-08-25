




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMaster extends APIResponseBean {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Set<RoleMaster> userRoles;

	

}

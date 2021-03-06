package ma.gymmanager.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Data;
import ma.gymmanager.dao.RoleRepository;
import ma.gymmanager.dao.UserRepository;
import ma.gymmanager.domaine.RoleConverter;
import ma.gymmanager.domaine.RoleVo;
import ma.gymmanager.domaine.UserConverter;
import ma.gymmanager.domaine.UserVo;
import ma.gymmanager.model.Role;
import ma.gymmanager.model.User;

@Service("userService")
@Data
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        boolean enabled = true;
        boolean accountNotExpired = true;
        boolean credentialsNotExpired = true;
        boolean accoutnNotLocked = true;
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), enabled,
                accountNotExpired, credentialsNotExpired, accoutnNotLocked, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> springSecurityAuthorities = new ArrayList<>();
        for (Role r : roles) {
            springSecurityAuthorities.add(new SimpleGrantedAuthority(r.getNom()));
        }
        return springSecurityAuthorities;
    }

    @Override
    public void add(UserVo user) {
        User u = UserConverter.toBo(user);
        u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        List<Role> rolesPersist = new ArrayList<>();
        for (Role role : u.getRoles()) {
            Role userRole = roleRepository.findByNom(role.getNom()).get(0);
            rolesPersist.add(userRole);
        }
        u.setRoles(rolesPersist);
        userRepository.save(u);
    }

    @Override
    public void save(UserVo user) {
        List<Role> rolesPersist = new ArrayList<>();
        User old = userRepository.getOne(user.getId());
        User u = UserConverter.toBo(user);
        if(old.getPassword()==null)
            old.setPassword("");
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa >"+u.getPassword());
            System.out.println("ccccccccccccccccccccccccccccccccccc >"+old.getPassword());
        if (!(old.getPassword().equals(u.getPassword())))
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
        for (Role role : u.getRoles()) {
            Role userRole = roleRepository.findByNom(role.getNom()).get(0);
            rolesPersist.add(userRole);
        }
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb >"+u.getPassword());
        u.setRoles(rolesPersist);
        userRepository.save(u);
    }

    @Override
    public void save(RoleVo role) {
        roleRepository.save(RoleConverter.toBo(role));
    }

    @Override
    public void delete(int userId) {
        roleRepository.deleteById(userId);
    }

    @Override
    public List<UserVo> getAlluUsers() {
        return UserConverter.toVoList(userRepository.findAll());
    }

    @Override
    public List<RoleVo> getAllRoles() {
        List<RoleVo> roleList = RoleConverter.toVoList(roleRepository.findAll());
        // List<RoleVo> roles=new ArrayList<>();
        // for (RoleVo r : roleList)
        // if (!(r.getNom().equals("ADHERENT")) && !(r.getNom().equals("COACH")))
        // roles.add(r);
        return roleList;
    }

    @Override
    public RoleVo getRoleByName(String nom) {
        return RoleConverter.toVo(roleRepository.findByNom(nom).get(0));
    }

    @Override
    public RoleVo getRoleById(int id) {
        return RoleConverter.toVo(roleRepository.getOne(id));
    }

    @Override
    public UserVo getUserById(int id) {
        return UserConverter.toVo(userRepository.getOne(id));
    }

    @Override
    public Page<User> getAlluUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<RoleVo> getAllRoles(int page, int size) {

        return null;
    }

}
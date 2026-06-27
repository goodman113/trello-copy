package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.dto.OAuthUserCreate;
import my.project.trellocopy.entity.enums.AuthProvider;
import my.project.trellocopy.entity.enums.UserRole;
import my.project.trellocopy.repository.AuthProviderRepository;
import my.project.trellocopy.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String fullName;
        String providerId;
        String avatarUrl;
        AuthProvider authProvider = new AuthProvider();

        if (provider.equals("google")) {
            email = (String) attributes.get("email");
            fullName = (String) attributes.get("name");
            providerId = (String) attributes.get("sub");
            avatarUrl = (String) attributes.get("picture");
            authProvider.setProvider("google");
            authProvider.setProviderUserId(providerId);
        } else if (provider.equals("github")) {
            email = (String) attributes.get("email");
            fullName = (String) attributes.get("name");
            if (fullName == null) {
                fullName = (String) attributes.get("login");
            }
            providerId = String.valueOf(attributes.get("id"));
            avatarUrl = (String) attributes.get("avatar_url");
            authProvider.setProvider("github");
            authProvider.setProviderUserId(providerId);
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        String finalFullName = fullName;
        User user = userRepository.findUserByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(finalFullName);
            newUser.setProviderId(providerId);
            newUser.setAvatarUrl(avatarUrl);
            newUser.setRole(UserRole.USER);
            User save = userRepository.save(newUser);
            authProvider.setUserId(save.getId());
            return save;
        });
        boolean b = authProviderRepository.existsAuthProviderByProviderAndProviderUserId(provider, providerId);
        if (!b){
            authProviderRepository.save(authProvider);
        }
        return oAuth2User;
    }

//    private User createOAuth2User(OAuthUserCreate oAuthUserCreate) {
//        User user = new User();
//        user.setEmail(oAuthUserCreate.getEmail());
//        user.setFullName(oAuthUserCreate.getFullName());
//        user.setUsername(oAuthUserCreate.getEmail());
//        user.setAuthProvider(AuthProvider.valueOf(oAuthUserCreate.getProvider()));
//        user.setProviderId(oAuthUserCreate.getProviderId());
//        user.setAvatarUrl(oAuthUserCreate.getPicture());
//        user.setRole(UserRole.USER);
//        user.setOnline(Boolean.TRUE);
//
//        return userRepository.save(user);
//    }

}

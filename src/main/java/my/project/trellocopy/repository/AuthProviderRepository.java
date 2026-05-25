package my.project.trellocopy.repository;

import my.project.trellocopy.entity.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    boolean existsAuthProviderByUserIdAndProviderAndProviderUserId(Long userId, String provider, String providerUserId);

    boolean existsAuthProviderByProviderAndProviderUserId(String provider, String providerUserId);
}

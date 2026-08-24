package az.librarycrudapi.repository;

import az.librarycrudapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
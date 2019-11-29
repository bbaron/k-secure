package app.core.userdetails

import app.domain.CalendarUser
import app.domain.CalendarUserDao
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils.createAuthorityList
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.GroupManager
import org.springframework.stereotype.Service
import java.lang.UnsupportedOperationException

class CalendarUserDetails(user: CalendarUser) : CalendarUser(user), UserDetails {
    private val roles: Collection<GrantedAuthority> = createAuthorities(user)

    override fun getAuthorities(): Collection<GrantedAuthority> = roles

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = email

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = passcode

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    companion object {
        private val ADMIN_ROLES = createAuthorityList("ROLE_ADMIN", "ROLE_USER")
        private val USER_ROLES = createAuthorityList("ROLE_USER")

        fun createAuthorities(calendarUser: CalendarUser): Collection<GrantedAuthority> =
                if (calendarUser.email.startsWith("admin")) ADMIN_ROLES else USER_ROLES

    }
}

typealias Grouper<R> = () -> List<R>

@Service
class CalendarUserDetailsService(private val calendarUserDao: CalendarUserDao) : UserDetailsService, GroupManager {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = calendarUserDao.findByEmail(username) ?: throw UsernameNotFoundException(username)
        return CalendarUserDetails(user)
    }

    override fun findAllGroups(): List<String> = GROUPS

    private fun <R> toList(groupName: String?, adminFun: Grouper<R>, userFun: Grouper<R>): List<R> {
        return when (groupName) {
            "ADMIN" -> adminFun()
            "USER" -> userFun()
            else -> listOf()
        }
    }

    override fun findGroupAuthorities(groupName: String?): List<GrantedAuthority> =
            toList(groupName, { ADMIN_ROLES }, { USER_ROLES })

    override fun findUsersInGroup(groupName: String?): List<String> =
            toList(groupName, { calendarUserDao.findAdminUsers() }, { calendarUserDao.findNonAdminUsers() })
                    .map(CalendarUser::email)

    override fun createGroup(groupName: String?, authorities: MutableList<GrantedAuthority>?) {
        throw UnsupportedOperationException("groups are immutable")
    }

    override fun removeGroupAuthority(groupName: String?, authority: GrantedAuthority?) {
        throw UnsupportedOperationException("groups are immutable")
    }

    override fun addUserToGroup(username: String?, group: String?) {
        throw UnsupportedOperationException("users are grouped by convention")
    }

    override fun renameGroup(oldName: String?, newName: String?) {
        throw UnsupportedOperationException("groups are immutable")
    }

    override fun deleteGroup(groupName: String?) {
        throw UnsupportedOperationException("groups are immutable")
    }

    override fun addGroupAuthority(groupName: String?, authority: GrantedAuthority?) {
        throw UnsupportedOperationException("groups are immutable")
    }

    override fun removeUserFromGroup(username: String?, groupName: String?) {
        throw UnsupportedOperationException("users are grouped by convention")
    }

    companion object {
        private val GROUPS = listOf("USER", "ADMIN")
        private val ADMIN_ROLES = createAuthorityList("ROLE_ADMIN", "ROLE_USER")
        private val USER_ROLES = createAuthorityList("ROLE_USER")
    }

}
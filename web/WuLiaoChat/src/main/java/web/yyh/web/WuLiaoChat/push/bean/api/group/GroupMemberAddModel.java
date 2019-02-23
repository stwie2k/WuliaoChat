package web.yyh.web.WuLiaoChat.push.bean.api.group;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yyhyplxyz Email:xxw9808@163.com
 * @version 1.0.0
 */
public class GroupMemberAddModel {

    @Expose
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public static boolean check(GroupMemberAddModel model) {
        return !(model.users == null
                || model.users.size() == 0);
    }
}

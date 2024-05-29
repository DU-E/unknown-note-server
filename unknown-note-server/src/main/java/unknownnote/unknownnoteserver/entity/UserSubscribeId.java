package unknownnote.unknownnoteserver.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class UserSubscribeId implements Serializable {
    private int userId;
    private int followingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSubscribeId)) return false;
        UserSubscribeId that = (UserSubscribeId) o;
        return getUserId() == that.getUserId() &&
                getFollowingId() == that.getFollowingId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFollowingId());
    }
}

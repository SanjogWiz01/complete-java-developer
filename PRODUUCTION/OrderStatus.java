import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    DRAFT,
    CONFIRMED,
    PAID,
    SHIPPED,
    CANCELLED;

    public boolean canMoveTo(OrderStatus nextStatus) {
        if (nextStatus == null) {
            return false;
        }

        Set<OrderStatus> allowedStatuses = switch (this) {
            case DRAFT -> EnumSet.of(CONFIRMED, CANCELLED);
            case CONFIRMED -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(SHIPPED);
            case SHIPPED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
        };

        return allowedStatuses.contains(nextStatus);
    }

    public boolean isTerminal() {
        return this == SHIPPED || this == CANCELLED;
    }
}

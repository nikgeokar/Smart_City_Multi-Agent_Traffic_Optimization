package traffic_light_agent;

import java.util.Objects;

public class Position {
    private int avenue;
    private int street;

    public Position(int avenue, int street) {
        this.avenue = avenue;
        this.street = street;
    }

    public int getAvenue() {
        return avenue;
    }

    public int getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return avenue == position.avenue && street == position.street;
    }

    @Override
    public int hashCode() {
        return Objects.hash(avenue, street);
    }
}
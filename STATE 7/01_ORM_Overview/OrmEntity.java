package unit7.orm;

/** Simple domain object used to explain ORM mapping. */
public class OrmEntity {
    private Long id;
    private String name;

    public OrmEntity() {}

    public OrmEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "OrmEntity{id=" + id + ", name='" + name + "'}";
    }
}

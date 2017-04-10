import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Ranger implements BasicInterface {
  public int id;
  public String name;
  public int badge;

  public Ranger(String name, int badge) {
    this.id = id;
    this.name = name;
    this.badge = badge;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getBadge(){
    return badge;
  }

  @Override
  public boolean equals(Object otherRanger) {
    if(!(otherRanger instanceof Ranger)) {
      return false;
    } else {
      Ranger newRanger = (Ranger) otherRanger;
      return this.getName().equals(newRanger.getName()) &&
             this.getBadge() == newRanger.getBadge();
    }
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO rangers(name, badge) VALUES (:name, :badge);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("badge", this.badge)
        .executeUpdate()
        .getKey();
    // } catch (IllegalArgumentException exception)  {
    //     return null;
    // need to add this to the vtl ->   System.out.println("Please enter only numbers.");
    }
  }

  public static List<Ranger> all() {
    String sql = "SELECT * FROM rangers;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Ranger.class);
    }
  }

  public static Ranger find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM rangers WHERE id = :id;";
      Ranger ranger = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Ranger.class);
      return ranger;
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

  public void update(String name, int badge) {
    try(Connection con = DB.sql2o.open()) {
      String updateName = "UPDATE rangers SET name = :name WHERE id = :id;";
      String updateBadge = "UPDATE rangers SET badge = :badge WHERE id = :id;";
      con.createQuery(updateName)
         .addParameter("name", name)
         .addParameter("id", id)
         .executeUpdate();
      con.createQuery(updateBadge)
         .addParameter("badge", badge)
         .addParameter("id", id)
         .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM rangers WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}

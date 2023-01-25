import com.pufferenco.async.Task;
import com.pufferenco.async.ThreadManager;

public class Testing {
   public static void main(String[] args) {

   }

   public static void Load(){
      try {
         Thread.sleep(14);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }
}
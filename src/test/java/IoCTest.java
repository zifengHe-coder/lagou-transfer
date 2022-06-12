import com.lagou.edu.SpringConfig;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IoCTest {
    @Test
    public void testIoC() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        AccountDao accountDao =(AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);
    }
}

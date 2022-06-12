import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IoCTest {
    @Test
    public void testIoC() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountDao accountDao =(AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);
    }
}

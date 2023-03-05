package cn.imqinhao.controller;

import cn.imqinhao.common.BaseContext;
import cn.imqinhao.common.R;
import cn.imqinhao.entity.EmailVerify;
import cn.imqinhao.entity.Employee;
import cn.imqinhao.entity.MailRequest;
import cn.imqinhao.entity.User;
import cn.imqinhao.service.SendMailService;
import cn.imqinhao.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;

/**
 * @author qinhao
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SendMailService sendMailService;

    private String verifyCode;

    /**
     * 用户登录
     * @param request
     * @param emailVerify
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody EmailVerify emailVerify) {
        System.out.println("邮箱：" + emailVerify.getEmail());
        System.out.println("验证码：" + emailVerify.getCode());

        // 2. 根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, emailVerify.getEmail());
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            // 用户未注册
            user = new User();
            user.setStatus(1);
            user.setName(generateUserName());
            user.setEmail(emailVerify.getEmail());
            userService.save(user);
        }
        // 用户不点击获取验证码直接登录
        if (null == emailVerify.getCode() || "".equals(emailVerify.getCode())) {
            return R.error("请先点击获取验证码");
        }
        // 点击获取验证码但验证码输入错误
        else if (!verifyCode.equalsIgnoreCase(emailVerify.getCode())) {
            return R.error("验证码错误");
        }
        // 5. 查询用户状态，如果为已禁用状态，则返回用户已禁用结果
        if (user.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        // 6. 登录成功，将用户id存入Session并返回登录成功结果
        BaseContext.setCurrentId(user.getId());
        request.getSession().setAttribute("user", user.getId());
        return R.success(user);
    }

    /**
     * 发送验证码
     * @param email 邮箱地址
     * @return 结果
     */
    @GetMapping("/sendMsg")
    public R<String> sendMsg(String email) {
        String code = createRandomCode(6);
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSendTo(email);   // 收件人
        mailRequest.setSubject("瑞吉外卖");
        mailRequest.setText("【瑞吉外卖】您本次的验证码为：" + code + "，有效期一分钟，请妥善保管");
        verifyCode = code;
        sendMailService.sendSimpleMail(mailRequest);
        return R.success("验证码发送成功，请注意查收");
    }

    /**
     * 退出登录
     * @param request
     * @return 结果
     */
    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出登录成功");
    }

    /**
     * 生成随机验证码
     * @param n 验证码位数
     * @return 验证码
     */
    public String createRandomCode(int n) {
        String code = "";
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int num = r.nextInt(10);
            code += num;
        }
        return code;
    }

    /**
     * 生成用户名
     */
    public String generateUserName() {
        Random random = new Random();
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏
        int i = random.nextInt(3);//可以根据这个数设置产生的男女比例
        if (i == 2) {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                //   name = "女-" + name + girl.substring(j, j + 2);
                name = name + girl.substring(j, j + 2);
            } else {
                name = name + girl.substring(j, j + 1);
            }
        } else {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                name = name + boy.substring(j, j + 2);
            } else {
                name = name + boy.substring(j, j + 1);
            }
        }
        return name;
    }
}

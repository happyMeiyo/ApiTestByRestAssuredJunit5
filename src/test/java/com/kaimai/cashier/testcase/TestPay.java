package com.kaimai.cashier.testcase;

import com.alibaba.fastjson.JSONObject;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.kaimai.cashier.api.OrderApplication;
import com.kaimai.cashier.api.PayApplication;
import com.kaimai.cashier.api.VipApplication;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("支付相关业务测试")
public class TestPay extends TestUser{

    static VipApplication vip = VipApplication.getInstance();
    OrderApplication order = OrderApplication.getInstance();
    PayApplication pay = new PayApplication();


    static Stream<Arguments> payExp() {
//        String goodsDetail = "[{\"discountAmount\":0,\"discountPrice\":0,\"isDiscountPrice\":false,\"isTemporaryGoods\":false,\"isVipPrice\":false,\"productCategoryIdList0\":9993623,\"productCategoryIdList1\":9993624,\"productId\":999181571,\"productName\":\"果脯\",\"productSkuId\":999181613,\"saleCount\":\"1\",\"salePrice\":1300,\"saleUnit\":\"份\",\"skuId\":999305760,\"skuTitle\":\"果脯\",\"skuVersion\":1}]";
        return Stream.of(
                arguments(null, 4, 1300, 1300, "支付条码或支付渠道必选！"),
                arguments("Future",4, 1300, 1300, "请选择支付渠道"),
                arguments("ACCOUNTING",99999, 1300, 1300, "不支持的记账渠道"),
                arguments("CASH", 4, 1301, 1300, "实际支付金额大于订单总金额，请查证后再试！"),
                arguments("CASH", 4, 1300, 1400, "订单总金额与商品列表不一致，请查证"),
                arguments("CASH", 4, 1300, 1311, "订单总金额与商品列表不一致，请查证"),
                arguments("CASH", 4, 1300, 1401, "订单金额异常,不能完成结算，您可以清除购物车商品后，重新加购，若此问题重复出现，请联系客服小二处理")
        );
    }
    @DisplayName("支付异常测试")
    @ParameterizedTest(name="支付参数：渠道{0},渠道id{1}，支付金额{2},订单金额{3}")
    @Description("支付异常场景测试")
    @MethodSource("payExp")
    void testPayExp(String PaymentChannel,Integer channelId,
                    Integer payAmount, Integer totalAmount, String errMsg){
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金

//        data.put("goodsDetail",goodsDetail);
        data.put("paymentChannel", PaymentChannel);
        data.put("channelId", channelId);
        data.put("payAmount", payAmount);
        data.put("totalAmount", totalAmount);

        String body=template("/com/kaimai/cashier/testcase/payTemplateForExp.json", data);
        System.out.println(body);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).
                then().
                    body("result.success", equalTo(false)).
                    body("result.errorMsg", equalTo(errMsg));

    }

    @Test
    @DisplayName("现金支付，无优惠")
    @Description("商品不享受优惠，现金支付")
    void testPayByCommonCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplate.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("现金支付(找零)，享商品优惠")
    @Description("商品享受品类折扣，现金支付，找零")
    void testPayWithDiscountForCategoryByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForCategory.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("记账(学生卡)支付，享商品优惠和全场活动")
    @Description("商品享受品类折扣，订单享受全场活动(订单优惠包含享品类折扣商品)，现金支付")
    void testPayWithDiscountForCategoryAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是记账，校园卡
        data.put("paymentChannel", "ACCOUNTING");
        data.put("channelId", "47");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForCatgyAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("现金支付(找零)，享商品优惠和订单优惠")
    @Description("商品享受单品折扣、单品优惠，订单享受全场活动、整单优惠(订单优惠包含享单品优惠商品)，现金支付")
    void testPayWithDiscountForEvyGoodAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForEvyGoodAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("现金支付，享商品优惠与整单优惠")
    @Description("商品享受单品优惠，订单享受整单优惠，现金支付")
    void testPayWithDiscountForEveryGoodAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是记账，银行卡
        data.put("paymentChannel", "ACCOUNTING");
        data.put("channelId", "48");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForEveryGoodAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.pay(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("现金支付，享商品折扣与全场活动")
    @Description("商品价格改高、改低，订单享全场活动，现金支付")
    void testPayWithDiscountForGoodsAndOrderByCash() {
        HashMap<String, Object> data = new HashMap<>();
        //支付方式是现金
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDiscountForGoodsAndOrder.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payWithDiscountForOrder(params).
                then().body("result.success", equalTo(true));
    }


    @Test
    @DisplayName("会员现金支付，有会员优惠")
    @Description("商品享受会员优惠(会员价)，会员现金支付")
    void testPayByCashForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员现金支付
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);

        pay.payForVip(params).
                then().body("result.success", equalTo(true));
    }


    @Test
    @DisplayName("会员余额支付，有会员优惠和单品优惠")
    @Description("商品享受单品优惠和会员优惠(会员价)，会员余额支付")
    void testPayWithDiscountForEveryGoodAndOrderByCardForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员卡余额支付
        data.put("paymentChannel", "VIPCARD");
        data.put("channelId", "0");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndOrderForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.payForVip(params).
                then().body("result.success", equalTo(true));
    }

    @Test
    @DisplayName("会员现金支付，有会员优惠和单品优惠")
    @Description("商品享受单品优惠和会员优惠(会员价)，会员现金支付")
    void testPayWithDiscountForEveryGoodAndOrderByCashForVip() {
        HashMap<String, Object> data = new HashMap<>();
        String vipPayToken = "";
        String vipCardNo = vip.getVipCardNo();
        try {
            vipPayToken = vip.getDetailOfVip(vipCardNo).then().extract().path("data.vipPayToken");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //会员现金支付
        data.put("paymentChannel", "CASH");
        data.put("channelId", "4");
        data.put("vipCardNo", vipCardNo);
        data.put("vipPayToken", vipPayToken);

        String body=template("/com/kaimai/cashier/testcase/payTemplateWithDistForEvyGoodAndOrderForVip.json", data);
        JSONObject params = JSONObject.parseObject(body);
        pay.payForVip(params).
                then().body("result.success", equalTo(true));
    }


    public String template(String templatePath, HashMap<String, Object> data){
        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(this.getClass().getResource(templatePath).getPath());
        mustache.execute(writer, data);
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return writer;
        return writer.toString();
    }
}

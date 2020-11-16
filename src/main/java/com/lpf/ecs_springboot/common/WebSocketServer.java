package com.lpf.ecs_springboot.common;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.entity.Touzi;
import com.lpf.ecs_springboot.entity.User;
import com.lpf.ecs_springboot.service.TouziService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/WebSocket/{userId}")
@Component
public class WebSocketServer {
    static Log log= LogFactory.get(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId;
    private TouziService touziService;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) throws IOException {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+userId+",报文:"+message);
        try {
            //解析发送的报文
            JSONObject jsonObject = (JSONObject) JSON.parse(message);
            String type = (String) jsonObject.get("type");
            if (type.equals("CallAPI"))
            {
                String classname = jsonObject.get("classname").toString();
                String method = jsonObject.get("method").toString();
                String parameter = jsonObject.get("parameter").toString();
                String username = jsonObject.get("username").toString();
                String fromId = jsonObject.get("fromId").toString();
                log.info("用户"+fromId+"请求："+classname+"."+method);
                log.info("请求参数："+JSON.parseObject(parameter));
                Class c = SpringContextUtil.getBean(classname).getClass();
               // Object Obj = c.newInstance();
                Method m = c.getMethod(method, new Class[] {String.class});
                JSONObject resMap = (JSONObject) m.invoke( SpringContextUtil.getBean(classname) , parameter);
                resMap.put("classname",classname);
                resMap.put("method",method);
                resMap.put("type","CallAPI");
                log.info("返回参数："+resMap);
                if(classname.equals("TouziService") && method.equals("palyGame"))
                {
                    if(resMap.get("flag").equals("ok"))
                    {
                        sendMessageAll(JSON.toJSONString(resMap));
                        JSONObject sendMsg = new JSONObject();
                        sendMsg.put("type","sendMsg");
                        sendMsg.put("fromId",-1);
                        sendMsg.put("fromName","系统通知");
                        sendMsg.put("ishost",0);
                        sendMsg.put("msg",username+"，开始了游戏");
                        sendMessageAll(JSON.toJSONString(sendMsg));
                    }
                    else {
                        sendMessageToUid(JSON.toJSONString(resMap),fromId);
                    }
                }
                else if(classname.equals("TouziService") && method.equals("overGame")){
                    if(resMap.get("flag").equals("ok"))
                    {
                        sendMessageAll(JSON.toJSONString(resMap));
                    }
                    else {
                        sendMessageToUid(JSON.toJSONString(resMap),fromId);
                    }
                }
                else if(classname.equals("TouziService") && method.equals("leaveRoom")){
                    if(resMap.get("flag").equals("ok"))
                    {
                        sendMessageAll(JSON.toJSONString(resMap));
                        JSONObject sendMsg = new JSONObject();
                        sendMsg.put("type","sendMsg");
                        sendMsg.put("fromId",-1);
                        sendMsg.put("fromName","系统通知");
                        sendMsg.put("ishost",0);
                        sendMsg.put("msg",username+"，离开了房间");
                        sendMessageAll(JSON.toJSONString(sendMsg));
                    }
                    else {
                        sendMessageToUid(JSON.toJSONString(resMap),fromId);
                    }
                }
                else if(classname.equals("TouziService") && method.equals("enterTouziRoom")){
                    if(resMap.get("flag").equals("ok"))
                    {
                        sendMessageAll(JSON.toJSONString(resMap));
                        JSONObject sendMsg = new JSONObject();
                        sendMsg.put("type","sendMsg");
                        sendMsg.put("fromId",-1);
                        sendMsg.put("fromName","系统通知");
                        sendMsg.put("ishost",0);
                        sendMsg.put("msg",username+"，进入了房间");
                        sendMessageAll(JSON.toJSONString(sendMsg));
                    }
                    else {
                        sendMessageToUid(JSON.toJSONString(resMap),fromId);
                    }
                }

                else if(classname.equals("TouziService") && method.equals("openGame")){
                    if(resMap.get("flag").equals("ok"))
                    {
                        for (String s : (List<String>)resMap.get("data")){
                            JSONObject sendMsg = new JSONObject();
                            sendMsg.put("type","sendMsg");
                            sendMsg.put("fromId",-1);
                            sendMsg.put("fromName","系统通知");
                            sendMsg.put("ishost",0);
                            sendMsg.put("msg",s);
                            sendMessageAll(JSON.toJSONString(sendMsg));
                        }
                    }
                    else {
                        sendMessageToUid(JSON.toJSONString(resMap),fromId);
                    }
                }
                else
                {
                    sendMessageToUid(JSON.toJSONString(resMap),fromId);
                }

            }
            else if (type.equals("sendMsg"))
            {
                String Msg = jsonObject.get("Msg").toString();
                String fromId = jsonObject.get("fromId").toString();
                String fromName = jsonObject.get("fromName").toString();
                String ishost = jsonObject.get("ishost").toString();
                JSONObject resMap = new JSONObject();
                resMap.put("type","sendMsg");
                resMap.put("fromId",fromId);
                resMap.put("fromName",fromName);
                resMap.put("ishost",ishost);
                resMap.put("msg",Msg);
                sendMessageAll(JSON.toJSONString(resMap));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException, EncodeException {
        log.info("发消息："+message);
        this.session.getBasicRemote().sendText(message);
    }
    public void sendMessageAll(String message) throws IOException, EncodeException {
        log.info("群发消息："+message);
        for (String key : webSocketMap.keySet()) {
            webSocketMap.get(key).sendMessage(message);
        }
    }

    /**
     * 发送自定义消息
     * */
    public static void sendMessageToUid(String message,@PathParam("userId") String userId) throws Exception {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

package com.intl.webview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.intl.entity.IntlDefine;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class WebSession {

    private static final String YC_WEB_GENERAL_COMMAND_DOMAIN = "yc.mobilesdk.web";

    private static WebSession _currentWebSession;

    public WebSession(){
        super();
        _webDailog = null;//new WeakReference<IWebPage>(null);
        _webActivity = null;//new WeakReference<IWebPage>(null);
        registClose();
        registCloseAll();
    }
    private void registClose()
    {
        this.regisetCommandListener(YC_WEB_GENERAL_COMMAND_DOMAIN,
                "close",
                new IWebCommandListener() {
                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        if (sender.getWebPage() != null) {
                            sender.getWebPage().close();
                            sender.onWebCommandResponse(IntlDefine.YC_SDK_WEB_VIEW_DOMAIN |
                                            IntlDefine.YC_SDK_GENERAL_WEB_COMMAND_MODULE |
                                            IntlDefine.YC_SDK_MSG,
                                    "success", null);
                        }
                    }
                });
    }
    private void registCloseAll()
    {
        this.regisetCommandListener(YC_WEB_GENERAL_COMMAND_DOMAIN,
                "closeall",
                new IWebCommandListener() {
                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        if (_webActivity != null) {
                            IWebPage webPage = _webActivity;
                            if (webPage != null) {
                                webPage.close();
                            }
                        }
                        if (_webDailog != null) {
                            IWebPage dialog = _webDailog;
                            if (dialog != null) {
                                dialog.close();
                            }
                        }
                        sender.onWebCommandResponse(IntlDefine.YC_SDK_WEB_VIEW_DOMAIN |
                                        IntlDefine.YC_SDK_GENERAL_WEB_COMMAND_MODULE |
                                        IntlDefine.YC_SDK_MSG,
                                "success", null);
                    }
                });


    }
    public static void setDialogVisiable(boolean visiable) {
        WebSession currentWebSession = getCurrentWebSession();
        if (null == currentWebSession) {
            return;
        }
        IWebPage webPage = currentWebSession._webDailog;
        if (null == webPage) {
            return;
        }
        Dialog dialog = (Dialog) webPage;
        if (visiable) {
            dialog.show();
        } else  {
            dialog.hide();
        }
    }
    public static  boolean getIsShwoingWebPage() {
        return _currentWebSession != null;
    }
    private static void setCurrentWebSession(WebSession webSession) {
        _currentWebSession = webSession;
    }
    private static WebSession getCurrentWebSession() {
        return _currentWebSession;
    }
    public static WebSession currentWebSession() {
        return _currentWebSession;
    }
    private IWebSessionListener _webSessionListener;
    public void setWebSessionListener(IWebSessionListener listener) {
        _webSessionListener = listener;
    }
    private IWebPage _webActivity;
    private IWebPage _webDailog;
    public void showDialog(Context currentActivity, int width, int hight, Uri uri, boolean enableBackKey)
    {
        if (getIsShwoingWebPage() == true && this != _currentWebSession) {
            return;
        }
        if ( null != _webActivity || null != _webDailog) {
            return;
        }
        setCurrentWebSession(this);
        WebDialog dialog = new WebDialog(currentActivity, uri,width, hight, enableBackKey, this);
        dialog.setCanceledOnTouchOutside(enableBackKey);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                _webDailog = null;
                if (null == _webDailog && null == _webActivity) {
                    setCurrentWebSession(null);
                    if (null != _webSessionListener) {
                        _webSessionListener.onWebSessionClosed();
                    }
                }
            }
        });
        dialog.show();
    }

    public void forceCloseSession() {
        if (_webActivity != null) {
            IWebPage webPage = _webActivity;
            if (webPage != null) {
                webPage.close();
            }
        }
        if (_webDailog != null) {
            IWebPage dialog = _webDailog;
            if (dialog != null) {
                dialog.close();
            }
        }
    }

    protected void onActivityClose() {
        _webActivity = null;
        if (null == _webDailog && null == _webActivity) {
            setCurrentWebSession(null);
            if (null != _webSessionListener) {
                _webSessionListener.onWebSessionClosed();
            }
        }
    }

    protected void onActivityOpen(WebActivity activity) {
        _webActivity = activity;
    }

    protected void onDialogOpen(WebDialog dialog) {
        _webDailog = dialog;
    }
    private Hashtable<String, Hashtable<String, IWebCommandListener>> _regedCommandDomain =
            new Hashtable<String, Hashtable<String, IWebCommandListener>>();
    public void regisetCommandListener(String commandDomain, String command, IWebCommandListener listener) {

        if (listener == null)
        {
            if (_regedCommandDomain.containsKey(commandDomain)) {
                Hashtable<String, IWebCommandListener> commandTable =
                        _regedCommandDomain.get(commandDomain);
                if (commandTable.containsKey(command)) {
                    commandTable.remove(command);
                }
                if (commandTable.keySet().size() == 0) {
                    _regedCommandDomain.remove(commandDomain);
                }
            }
        } else {
            Hashtable<String, IWebCommandListener> commandTable = null;
            if (!_regedCommandDomain.containsKey(commandDomain)) {
                commandTable = new Hashtable<>();
                _regedCommandDomain.put(commandDomain, commandTable);
            } else {
                commandTable = _regedCommandDomain.get(commandDomain);
            }
            commandTable.put(command,listener);
        }
    }
    public IWebCommandListener getCommand(String commandDomain, String command) {
        Hashtable<String, WebSession.IWebCommandListener> commandTable =
                _regedCommandDomain.get(commandDomain);

        if (null != commandTable && commandTable.containsKey(command)) {
            WebSession.IWebCommandListener listener = commandTable.get(command);
            return listener;
        }
        return  null;
    }
    void onWebPageLoadFailed(WebCommandSender sender, int errorCode,
                             String description, String failingUrl) {
        if (null != _webSessionListener) {
            _webSessionListener.onWebPlageLoadFailed(sender, errorCode, description, failingUrl);
        }
    }
    public interface IWebCommandListener
    {
        void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args);
    }
    public interface IWebSessionListener {
        void onWebPlageLoadFailed(WebCommandSender sender, int errorCode,
                                  String description, String failingUrl);

        void onWebSessionClosed();
    }
}

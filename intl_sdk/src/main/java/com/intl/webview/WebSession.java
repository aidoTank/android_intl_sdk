package com.intl.webview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class WebSession {


    private static WebSession _currentWebSession;

    public WebSession() {
        super();
        _webDailog = null;//new WeakReference<IWebPage>(null);
        _webActivity = null;//new WeakReference<IWebPage>(null);
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
            }
        });
        dialog.show();
    }

    public void forceCloseSession() {
        if (_webDailog != null) {
            IWebPage dialog = _webDailog;
            if (dialog != null) {
                _webDailog = null;
                setCurrentWebSession(null);
                if (null != _webSessionListener) {
                    _webSessionListener.onWebSessionClosed();
                }
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

    protected void onDialogOpen(WebDialog dialog) {
        _webDailog = dialog;
    }
    private Hashtable<String, Hashtable<String, IWebCommandListener>> _regedCommandDomain =
            new Hashtable<String, Hashtable<String, IWebCommandListener>>();
    private IWebCommandListener iWebCommandListener;
    public void regisetCommandListener(IWebCommandListener listener)
    {
        iWebCommandListener = listener;
    }
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
    public IWebCommandListener getCommand()
    {
        if(iWebCommandListener != null)
            return iWebCommandListener;
        return null;
    }
    public IWebCommandListener getCommand(String commandDomain, String command) {
        Hashtable<String, IWebCommandListener> commandTable =
                _regedCommandDomain.get(commandDomain);

        if (null != commandTable && commandTable.containsKey(command)) {
            IWebCommandListener listener = commandTable.get(command);
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

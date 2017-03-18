package com.yangsion.runandroid.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
 
/**
 * @copy 对话框封装类  Wrapper class dialog box
 */
public class DialogUtil {
  
    /**
     * 创建普通对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param message 显示内容 必填
     * @param btnName 按钮名称 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createNormalDialog(Context ctx, 
   int iconId, 
   String title, 
   String message, 
   String btnName, 
   OnClickListener listener) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 设置对话框的显示内容
  builder.setMessage(message);
  // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setPositiveButton(btnName, listener);
  // 创建一个普通对话框
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * 创建列表对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createListDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   OnClickListener listener) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setItems(itemsId, listener);
  // 创建一个列表对话框
  dialog = builder.create();
  return dialog;
 }
  
    /**
     * 创建单选按钮对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param listener 单选按钮项监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param btnName 按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createRadioDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   OnClickListener listener,
   String btnName,
   OnClickListener listener2) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 0: 默认第一个单选按钮被选中
  builder.setSingleChoiceItems(itemsId, 0, listener);
  // 添加一个按钮
  builder.setPositiveButton(btnName, listener2) ;
  // 创建一个单选按钮对话框
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * 创建复选对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param flags 初始复选情况 必填
     * @param listener 单选按钮项监听器，需实现android.content.DialogInterface.OnMultiChoiceClickListener接口 必填
     * @param btnName 按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createCheckBoxDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   boolean[] flags,
   android.content.DialogInterface.OnMultiChoiceClickListener listener,
   String btnName,
   OnClickListener listener2) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  builder.setMultiChoiceItems(itemsId, flags, listener);
  // 添加一个按钮
  builder.setPositiveButton(btnName, listener2) ;
  // 创建一个复选对话框
  dialog = builder.create();
  return dialog;
 }
 
 /**
  * 创建一个编辑对话框
  * 
  * @param ctx
  * @param iconId
  * @param title
  * @param message
  * @param itemsId
  * @param listener
  * @param btnName
  * @param listener2
  * @return
  */
 public static Dialog createEditDialog(Context ctx, 
		 int iconId, 
		 EditText message,
		 String title, 
		 int itemsId, 
		 String btnName,
		 OnClickListener listener) {
	 Dialog dialog=null;
	 android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
	 // 设置对话框的图标
	 builder.setIcon(iconId);
	 // 设置对话框的标题
	 builder.setTitle(title);
	 // 添加一个按钮
	 builder.setPositiveButton(btnName, listener) ;
	 // 创建一个编辑对话框
	 dialog = builder.create();
	 return dialog;
 }
 
}

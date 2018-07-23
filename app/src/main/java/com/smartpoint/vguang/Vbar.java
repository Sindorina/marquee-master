package com.smartpoint.vguang;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class Vbar {

	public interface Vdll extends Library {

		Vdll INSTANCE = (Vdll) Native.loadLibrary("vbar", Vdll.class);
		//连接设备
		public IntByReference vbar_connectDevice(long arg);
		//背光灯控制
		public int vbar_backlight(IntByReference vbar_channel, boolean bswitch);
		//间隔时间设置
		public int vbar_interval(IntByReference vbar_channel, int time);
		//码制添加
		public int  vbar_addCodeFormat(IntByReference vbar_channel, byte codeformat);
		//蜂鸣器控制
		public int  vbar_beepControl(IntByReference vbar_channel, byte times);
		//版本号获取
		public int  vbar_getVersion(IntByReference vbar_channel, byte[] result_buffer, IntByReference result_size);
		//获取扫描结果
		public int  vbar_getResultStr(IntByReference vbar_channel, byte[] result_buffer, IntByReference result_size, IntByReference result_type);
		//关闭设备
		public void  vbar_disconnectDevice(IntByReference vbar_channel);
		//获取设备号接口
		public int  vbar_get_devnum(IntByReference vbar_channel, byte[] result_buffer);
	}

	//初始化设备变量
	IntByReference vbar_channel = null;




	byte [] version_buffer = new byte[1024];
	IntByReference version_size = new IntByReference(1024);

	byte [] devnum_buffer = new byte[1024];

	//连接设备
	public boolean vbarOpen() {
		if(vbar_channel == null)
		{
			vbar_channel = Vbar.Vdll.INSTANCE.vbar_connectDevice(1);
		}
		if (vbar_channel != null) {
			System.out.println("open device success");
			return true;
		} else {
			System.out.println("open device fail");
			return false;
		}
	}
	//背光灯控制
	public boolean vbarLight(boolean on)
	{
		int state = Vbar.Vdll.INSTANCE.vbar_backlight(vbar_channel,on);
		if(state == 0)
		{
			System.out.println("light  success");
			return true;
		}
		else
		{
			System.out.println("light  fail");
			return false;
		}
	}
	//间隔时间设置
	public void vbarInterval(int time)
	{
		int state = Vdll.INSTANCE.vbar_interval(vbar_channel,time);
		if(state == 0)
		{
			System.out.println("interval success");
		}
		else
		{
			System.out.println("interval  fail");
		}
	}
	//码制添加
	public boolean vbarAddSymbolType(byte symbol_type){
		if(Vdll.INSTANCE.vbar_addCodeFormat(vbar_channel,symbol_type) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//蜂鸣器控制
	public boolean vbarBeep(byte times){
		if(Vdll.INSTANCE.vbar_beepControl(vbar_channel,times) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//版本号获取
	public String vbarGetversion()
	{
		String device_version = null;
		if(Vdll.INSTANCE.vbar_getVersion(vbar_channel,version_buffer,version_size) == 0)
		{
			device_version = new String(version_buffer);
			return device_version;
		}
		else
		{
			return null;
		}

	}
	//获取设备号接口
	public String vbarGetdevnum()
	{
		String device_num = null;
		if(Vdll.INSTANCE.vbar_get_devnum(vbar_channel,devnum_buffer) == 0)
		{
			device_num = new String(devnum_buffer);
			return device_num;
		}
		else
		{
			return null;
		}
	}

	//关闭设备
	public void closeDev()
	{
		if(vbar_channel != null)
		{
			Vbar.Vdll.INSTANCE.vbar_disconnectDevice(vbar_channel);
			System.out.println("close success");
		}
	}

	//获取扫描结果
	public String getResultsingle(){
		byte [] result_buffer = new byte[1024];
		IntByReference result_size = new IntByReference(1024);
		IntByReference result_type = new IntByReference(1024);
		if(Vdll.INSTANCE.vbar_getResultStr(vbar_channel,result_buffer,result_size,result_type) == 0)
		{
			String decode = null;
			decode = new String(result_buffer);
			int size = result_size.getValue();
			//System.out.println("###########################" + size);
			//System.out.println("解析结果-->" + decode.trim());
			result_buffer = null;
			result_size = null;
			result_type = null;
			return decode;
		}
		else
		{
			return null;
		}
	}
}



	
	


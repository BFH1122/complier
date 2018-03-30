package com;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
	static Symbol_list symbol=new Symbol_list();//符号集
	static Symbol_list pdown=symbol;//符号及指针
	
	static Token_list Token=new Token_list();//Token字符表
	static Token_list pnext=Token;//token指针
	
	static int symbol_pos = 0;//记录符号表下标
	
	static int m=0; //记录源程序 的行数
	
	static String error_mes="";
	
	static String biaoliang="666";
	static String error="444";
	static String zhushi="555";
	static String string="777";
	
	static String mychar="333";
	static String myint="111";
	static String myfloat="222";
	
	public static String keywords[] = {"int","float","string","char","if","else","elsif","break","continue","main","void"};//关键字对用的标号为再数组中对应的下标的位置

	public static char operator[] = { '+', '-', '*','&', '|','=', '<', '>', '(', ')', '[', ']', '{', '}',';',',', };
	//运算符的标号是其对应的下标+sizeof(keywords)；如果是双字符则特殊定义
	public static char boundary[] = { '(', ')', '[', ']', '{', '}',';',',', };
	
	public static String zhuanyi[]={"'\\n'","'\\t'","'\\\\'"};//定义的转义字符
	public static boolean search_zhuanyi(String ch)
	{
		for(int i=0;i<zhuanyi.length;i++)
		{
			if(ch.equals(zhuanyi[i]))
				return true;
		}
		return false;
	}
	public static void print(){
		Token_list p1=Token;
		System.out.println("-----------字符表---------------");
		while(!p1.getKey().equals("-1"))
		{
			System.out.println(p1.get());
			p1=p1.pnext;
		}
		Symbol_list p2=symbol;
		System.out.println("-----------符号表---------------");
		while(p2.getpos()!=-1)
		{
			System.out.println(p2.get());
			p2=p2.pdown;
		}
		System.out.println("----------报错信息----------------");
		System.out.print(error_mes);
		
	}
	public static void WriteStringToFile() {  
        try {  
            
            String str="-----------字符表---------------"+System.getProperty("line.separator");
            Token_list p1=Token;
//    		System.out.println("-----------字符表---------------");
    		while(!p1.getKey().equals("-1"))
    		{
    			str+=p1.get()+System.getProperty("line.separator");
    			//System.out.println(p1.get());
    			p1=p1.pnext;
    		}
    		File file = new File("D://Token.txt");  
            PrintStream ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(str);
    		
    		str="-----------符号表---------------"+System.getProperty("line.separator");
    		Symbol_list p2=symbol;
//    		System.out.println("-----------符号表---------------");
    		while(p2.getpos()!=-1)
    		{
    			str+=p2.get()+System.getProperty("line.separator");
    			//System.out.println(p2.get());
    			p2=p2.pdown;
    		}
    		file = new File("D://symbol.txt");  
           ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(str);
    		
    		str="----------报错信息----------------";
//    		System.out.println("----------报错信息----------------");
//    		System.out.print(error_mes);
    		file = new File("D://error_mes.txt");  
            ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(error_mes);
    		
    		
            // 往文件里写入字符串  
//            ps.append("http://www.jb51.net");// 在已有的基础上添加字符串  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
	public static void main(String[] args) throws IOException
	{
		print_biaohao();
		
		String text=readFile(); //读取指定的文件信息
		Lex_analyze(text);
		print();
		WriteStringToFile();
	}
	public static String[] line_text;
	public static void Lex_analyze(String text)
	{
		line_text=text.split("\n");
		for(m=0;m<line_text.length;m++)//按行处理文件内容
		{
			String str = line_text[m];//取得一行的程序代码
			if(str.equals("")) //表示有空行
				continue;
			else{
				char[] strline = str.toCharArray();
				for(int i=0;i<strline.length;i++)
				{
					char ch = strline[i];
					String token = "";
					if(isAlpha(ch))//当它是标识符或者关键时调用
					{
						i=do_ALpha(strline,i);
					}
					//判断是不是数字常量
					else if(isDigit(ch))
					{
						
						i=do_Dight(strline,i);
					}
					else if(ch == '\'')
					{
						i=do_char(strline,i);
					}
					else if (ch == '"')
					{
						i=do_string(strline,i);
					}
					else if(isOp(ch))
					{
						i=do_op(strline,i);
					}
					else if (ch == '/')
					{
						i=do_note(strline,i,str);
					}
					
				}	
			}
		}
	}
	public static void print_biaohao()
	{
		int i=0;
		int len=keywords.length;
		for(i=0;i<len;i++)
		{
			System.out.println("\t"+keywords[i]+"\t"+i);
		}
		for(i=0;i<operator.length;i++)
		{
			System.out.println("\t"+operator[i]+"\t"+(i+len+1));
		}
		System.out.println("\t"+"标量"+"\t"+biaoliang);
		System.out.println("\t"+"error"+"\t"+error);
		System.out.println("\t"+"注释"+"\t"+zhushi);
	}
	
	public static int do_note(char []strline,int i,String str)
	{
		 
		String token="";
		char ch=strline[i];
		token += ch;  
        i++;
        ch = strline[i];
        
		//不是注释的情况，有可能是除法
        if (ch != '*' && ch != '/')   
        {
        	
            //int tmp=in_operation(token);
        	//pnext.add(1100+tmp,token);
            pnext.add(11+"", token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			i=i-1;
        }
        // 注释可能是‘//’也可能是‘/*’
        else 
        {
        	int State = 0;
        	if (ch == '*')//判定是/*的情况 
        	{
        		int k=0;
        		while(true)
        		{
        			if(i<strline.length-1&&(((strline[i]+"").equals("*")&&(strline[i+1]+"").equals("/"))))//不是终结符*/的时候，直接向下遍历
        			{
        				token+=strline[i+1];
        				pnext.add("s"+zhushi, "//");
        				pnext.pnext=new Token_list();
        				pnext=pnext.pnext;
        				break;
        			}
        			i++;
        			if(i==strline.length)
        			{
        				m++;
        				i=0;
        				String strs = line_text[m];//取得一行的程序代码
        				strline = strs.toCharArray();
        			}
        		}
        		i++;
            }
        	else if(ch == '/')
        	{
        		//单行注释读取所有字符	
        		int index = str.lastIndexOf("//");  
                String tmpstr = str.substring(index);  
                int tmpint = tmpstr.length();  
                for(int k=0;k<tmpint;k++)                                     
                  i++;    
                token = tmpstr;
                pnext.add("d"+zhushi,"/"); 
    			pnext.pnext=new Token_list();
    			pnext=pnext.pnext;
        	}
        }	
        if(i==strline.length)
		{
			m++;
			i=0;
			String strs = line_text[m];//取得一行的程序代码
			strline = strs.toCharArray();
		}
		return i;
	}
	
	
	
	public static int in_boundary(String token)
	{
		int pos=-1;
		for(int i=0;i<boundary.length;i++)
		{
			if(token.equals(""+boundary[i]))
			{
				pos=i;
				break;
			}
		}
		return pos;
	}
	public static int do_op(char[] strline,int i)
	{
		String token="";
		char ch=strline[i];
		token += ch;
        //判断是否为界符
		int tmp_pos=in_boundary(token);
        if(tmp_pos!=-1)
        {
        	pnext.add(""+(tmp_pos+keywords.length+9), token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
        else
        {
        	int tmp=in_operation(token);
//        	System.out.println("打印信息"+keywords.length+"   "+tmp+"    "+ token);
        	pnext.add((1+keywords.length+tmp)+"",token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;	
        }
		return i;
	}
	
	public static int in_operation(String token)
	{
		int pos=-1;
		for(int i=0;i<operator.length;i++)
		{
			if(token.equals(operator[i]+""))
			{
				pos=i;
				break;
			}
		}
		return pos;
	}
	
	
	//b是指除了"之外的所有
	public static String stringDFA[] = { 
		"#b\"",
		"#b\"",
		"###"};
	public static boolean in_stringDFA(char ch, char key) {    
        if (key == '"')  
            return ch == key;  
        if (key == 'b')  
            return ch !='"' ;  
        return false;  
    }
	public static int do_string(char[] strline,int i)
	{
		char ch=strline[i];
		String tmp_token="";
		tmp_token+=ch;
        int state = 0;
        while(state!=2)
        {  
        	 i++;
             if(i==strline.length)
             {
             	
             	break;
             }
            ch=strline[i]; 
            for(int k = 0;k<3;k++)
            {  
                char tmpstr[] = stringDFA[state].toCharArray();  
                if (in_stringDFA(ch, tmpstr[k])) 
                {  
                    tmp_token += ch;  
                    state = k;  
                    break;  
                }  
            }
        }
        if(i>=strline.length-1)
        	ch=' ';
        else
        	ch=strline[i+1];
        if(state==2&&ch==' ')
        {
        	pnext.add(string,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;	
        }
        else if(state==1||state==0)
        {
        	pnext.add(string,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;	
        	error_mes+=m+"行字符串引号未封闭 "+tmp_token+"\n";
        }
        else{
        	pnext.add(string,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;	
        	error_mes+=m+"行字符串出界 "+tmp_token+ch+"\n";
        }
		return i;
	}
	//在字符中的空格必须使用'\0'来表示，遇见空格则会认为是下一个分析过程的开始
	public static int do_char(char[] strline,int i)
	{
		String tmp_token="";
		int state = 0;
		boolean length_flag=false;
		char ch=strline[i]; 
        while(ch!=' '&&i<strline.length)
        {
        	int k;
        	tmp_token += ch;
        	for( k=0;k<=7;k++)
        	{
        		char tmpstr[] = charDFA[state].toCharArray();
        		if (in_charDFA(ch, tmpstr[k]))
                { 
                    state = k;
                    
                    break;  
                }
        	}
        	if(k>7)
        	{
        		length_flag=true;
        	}
        	i++;
        	if(i==strline.length)
        		break;
        	ch=strline[i];
        }
        if(state==3&&!length_flag)
        {
        	pnext.add(mychar,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
        else if(state==2||state==1)
        {
        	error_mes+=m+"行引号未封闭 "+tmp_token+"\n";
        	pnext.add(mychar,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			
        }
        else if(length_flag)
        {
        	error_mes+=m+"行字符长度出错 "+tmp_token+"\n";
        	pnext.add(mychar,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
        else if(state==4)
        {
        	if(search_zhuanyi(tmp_token))
        	{
        		pnext.add("mychar",tmp_token);
    			pnext.pnext=new Token_list();
    			pnext=pnext.pnext;	
        	}
        	else{
        		error_mes+=m+"行引号转义出错 "+tmp_token+"\n";
        		pnext.add("mychar",tmp_token);
        		pnext.pnext=new Token_list();
        		pnext=pnext.pnext;
			}
        	//System.out.println("state4:  "+tmp_token+"  "+search_zhuanyi(tmp_token));
        }
        else if(state==7)
        {
        	error_mes+=m+"行引号外出错 "+tmp_token+"\n";
        	pnext.add("mychar",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
        else if(state==6)
        {
        	error_mes+=m+"行不能为空字符 "+tmp_token+"\n";
        	pnext.add("mychar",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }  
		return i;
	}
	public static boolean in_charDFA(char ch, char key) {  
        if (key == 'a')  
            return true;  
        if (key == '\\')  
            return ch == key;  
        if (key == '\'')  
            return ch == key;  
        if (key == 'b')  
            return ch != '\\' && ch != '\'';  
        return false;  
    }
	public static String charDFA[] = { 
		"#\'######", 
		"##b#\\#\'#", 
		"###\'####",
		"#######a",
		"####a\'##",
		"########",//永远不能到达的状态
		"########",
		"#######a"}; 
	//处理数字的程序
	public static int do_Dight(char[] strline,int i)
	{
		String tmp_token="";
		int state = 0;
		int k;
		char ch=strline[i];
        boolean isfloat = false;  
        while ( (ch != '\0') && (isDigit(ch) || ch == '.' || ch == 'e' || ch == '-'))
        {
        	
        	if (ch == '.' || ch == 'e')  
              isfloat = true;
            for (k = 0; k <= 6; k++) 
            {  
                char tmpstr[] = digitDFA[state].toCharArray();  
                if (ch != '#' && 1 == in_digitDFA(ch, tmpstr[k])) 
                {  
                    tmp_token += ch;  
                    state = k;  
                    break;  
                }  
            }
            if(k>6)
            {
            	while(ch!=' '&&ch!='\n'&&i<strline.length)
            	{
            		ch=strline[i];
            		tmp_token+=ch;
            		i++;
            	}
            	error_mes+=m+"行常数发生错误\n";
            	pnext.add(myfloat,tmp_token);
    			pnext.pnext=new Token_list();
    			pnext=pnext.pnext;
    			
            	return i-2;
            }
            i++;
            if(i==strline.length)
            {
            	ch='\0';
            	break;
            }
            ch=strline[i];  	
        }
        //System.out.println("跳出来的时候"+strline[i]);
        if(ch!=' '&&i!=strline.length&&!(isDigit(ch) || ch == '.' || ch == 'e' || ch == '-'))
        {
        	
        	while(ch!=' '&&ch!='\n'&&i<strline.length)
        	{
        		ch=strline[i];
        		tmp_token+=ch;
        		i++;
        	}
        	error_mes+=m+"行常数发生错误\n";
        	pnext.add("myfloat",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			
        	return i-2;
        }
        if(state==2||state==4||state==5)
    	{
    		//报错
        	error_mes+=m+"行常数发生错误\n";
        	pnext.add("myfloat",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
    		return i;
    	}
    	else if(state==1)
    	{
    		pnext.add(myint,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			return i;
    	}
    	else{
    		pnext.add(myfloat,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			return i;
    	}
        
	}
	
	
	//判断是否是关键字
	public static int isKeyword(String str) {  
        int flag = -1;  
        for (int i = 0; i < keywords.length; i++) {  
            if (str.equals(keywords[i])) {  
                flag = i;  
                break;  
            }  
        }  
        return flag;  
    }
	
	//处理关键字和标识符
	public static int do_ALpha(char[] strline,int i)
	{
		
		String tmp_token="";
		char ch=strline[i];
		do {  
            tmp_token += ch;  
            i++;  
            if(i >= strline.length) 
            	break;  
            ch = strline[i];  
        } while (ch != '\0' && (isAlpha(ch) || isDigit(ch)));
		--i;
		
		//如果是关键字
		int tmp_pos=isKeyword(tmp_token.toString());
		//System.out.println("Token字："+tmp_token+"  "+tmp_pos);
		if (-1!=tmp_pos)//表明是关键字   
        {  
			pnext.add(""+tmp_pos,"");
//			pnext.add(""+tmp_token, "-1");
			//System.out.println("加入的："+pnext.get());
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
		else
		{	
			int tmp_pos1=search_symbol(tmp_token);
			if(tmp_pos1!=-1){
				pnext.add(biaoliang ,""+tmp_pos1);
				pnext.pnext=new Token_list();
				pnext=pnext.pnext;
			}
			else{
				pnext.add(biaoliang,""+symbol_pos);
				pnext.pnext=new Token_list();
				pnext=pnext.pnext;
				
				pdown.add(symbol_pos, tmp_token);
				pdown.pdown=new Symbol_list();
				pdown=pdown.pdown;
				symbol_pos++;
			}
		}
		return i;
	}
	//搜寻标识符的位置
	public static int search_symbol(String key)
	{
		int pos=-1;
		Symbol_list pt=symbol;
		//System.out.println("*********"+key);
		while(pt!=null&&pt.getpos()!=-1)
		{
			if(pt.getstr().equals(key))
			{
				pos=pt.getpos();
				break;
			}
			pt=pt.pdown;
		}
		return pos;
	}
	//程序的分步
	
	//识别数字的自动机
	public static String digitDFA[] = { 
		"#d#####", 
		"#d.#e##", 
		"###d###", 
		"###de##",  
        "#####-d", 
        "######d", 
        "######d" };
	public static int in_digitDFA(char ch, char test) 
	{  
        if (test == 'd') {  
            if (isDigit(ch))  
                return 1;  
            else  
                return 0;  
        }  
        else
        {
        	if (ch == test)
        		return 1;
        	else
        		return 0;
        }
    }
	
	
	//判断是否为字母及下滑线
	public static boolean isAlpha(char ch)
	{
	    return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
	}
	
	//判断是否是数字
	public static boolean isDigit(char ch)
	{  
        return (ch >= '0' && ch <= '9');  
    }
	//判断是否是运算符 
	public static boolean isOp(char ch) 
    {  
        for (int i = 0; i < operator.length; i++)  
            if (ch == operator[i]) {  
                return true;  
            }  
        return false;  
    }
	//读文件
	public static String readFile() throws IOException
	{
		Scanner in=new Scanner(System.in);
		//System.out.print("请选择文件的路径：");
		//String str=in.nextLine();
		String str="D:\\demo.txt";
		FileReader fr=null;
		try {
			fr=new FileReader(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("读取文件出错！");
			e.printStackTrace();
			return "false";
		}
		BufferedReader br=new BufferedReader(fr);
		String text="";
		String temp_text="";
		while((temp_text=br.readLine())!=null)
		{
			text=text+temp_text+"\n";
		}
		return text;
	}
}

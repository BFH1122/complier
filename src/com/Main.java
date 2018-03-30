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
	static Symbol_list symbol=new Symbol_list();//���ż�
	static Symbol_list pdown=symbol;//���ż�ָ��
	
	static Token_list Token=new Token_list();//Token�ַ���
	static Token_list pnext=Token;//tokenָ��
	
	static int symbol_pos = 0;//��¼���ű��±�
	
	static int m=0; //��¼Դ���� ������
	
	static String error_mes="";
	
	static String biaoliang="666";
	static String error="444";
	static String zhushi="555";
	static String string="777";
	
	static String mychar="333";
	static String myint="111";
	static String myfloat="222";
	
	public static String keywords[] = {"int","float","string","char","if","else","elsif","break","continue","main","void"};//�ؼ��ֶ��õı��Ϊ�������ж�Ӧ���±��λ��

	public static char operator[] = { '+', '-', '*','&', '|','=', '<', '>', '(', ')', '[', ']', '{', '}',';',',', };
	//������ı�������Ӧ���±�+sizeof(keywords)�������˫�ַ������ⶨ��
	public static char boundary[] = { '(', ')', '[', ']', '{', '}',';',',', };
	
	public static String zhuanyi[]={"'\\n'","'\\t'","'\\\\'"};//�����ת���ַ�
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
		System.out.println("-----------�ַ���---------------");
		while(!p1.getKey().equals("-1"))
		{
			System.out.println(p1.get());
			p1=p1.pnext;
		}
		Symbol_list p2=symbol;
		System.out.println("-----------���ű�---------------");
		while(p2.getpos()!=-1)
		{
			System.out.println(p2.get());
			p2=p2.pdown;
		}
		System.out.println("----------������Ϣ----------------");
		System.out.print(error_mes);
		
	}
	public static void WriteStringToFile() {  
        try {  
            
            String str="-----------�ַ���---------------"+System.getProperty("line.separator");
            Token_list p1=Token;
//    		System.out.println("-----------�ַ���---------------");
    		while(!p1.getKey().equals("-1"))
    		{
    			str+=p1.get()+System.getProperty("line.separator");
    			//System.out.println(p1.get());
    			p1=p1.pnext;
    		}
    		File file = new File("D://Token.txt");  
            PrintStream ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(str);
    		
    		str="-----------���ű�---------------"+System.getProperty("line.separator");
    		Symbol_list p2=symbol;
//    		System.out.println("-----------���ű�---------------");
    		while(p2.getpos()!=-1)
    		{
    			str+=p2.get()+System.getProperty("line.separator");
    			//System.out.println(p2.get());
    			p2=p2.pdown;
    		}
    		file = new File("D://symbol.txt");  
           ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(str);
    		
    		str="----------������Ϣ----------------";
//    		System.out.println("----------������Ϣ----------------");
//    		System.out.print(error_mes);
    		file = new File("D://error_mes.txt");  
            ps = new PrintStream(new FileOutputStream(file)); 
    		ps.println(error_mes);
    		
    		
            // ���ļ���д���ַ���  
//            ps.append("http://www.jb51.net");// �����еĻ���������ַ���  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
	public static void main(String[] args) throws IOException
	{
		print_biaohao();
		
		String text=readFile(); //��ȡָ�����ļ���Ϣ
		Lex_analyze(text);
		print();
		WriteStringToFile();
	}
	public static String[] line_text;
	public static void Lex_analyze(String text)
	{
		line_text=text.split("\n");
		for(m=0;m<line_text.length;m++)//���д����ļ�����
		{
			String str = line_text[m];//ȡ��һ�еĳ������
			if(str.equals("")) //��ʾ�п���
				continue;
			else{
				char[] strline = str.toCharArray();
				for(int i=0;i<strline.length;i++)
				{
					char ch = strline[i];
					String token = "";
					if(isAlpha(ch))//�����Ǳ�ʶ�����߹ؼ�ʱ����
					{
						i=do_ALpha(strline,i);
					}
					//�ж��ǲ������ֳ���
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
		System.out.println("\t"+"����"+"\t"+biaoliang);
		System.out.println("\t"+"error"+"\t"+error);
		System.out.println("\t"+"ע��"+"\t"+zhushi);
	}
	
	public static int do_note(char []strline,int i,String str)
	{
		 
		String token="";
		char ch=strline[i];
		token += ch;  
        i++;
        ch = strline[i];
        
		//����ע�͵�������п����ǳ���
        if (ch != '*' && ch != '/')   
        {
        	
            //int tmp=in_operation(token);
        	//pnext.add(1100+tmp,token);
            pnext.add(11+"", token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			i=i-1;
        }
        // ע�Ϳ����ǡ�//��Ҳ�����ǡ�/*��
        else 
        {
        	int State = 0;
        	if (ch == '*')//�ж���/*����� 
        	{
        		int k=0;
        		while(true)
        		{
        			if(i<strline.length-1&&(((strline[i]+"").equals("*")&&(strline[i+1]+"").equals("/"))))//�����ս��*/��ʱ��ֱ�����±���
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
        				String strs = line_text[m];//ȡ��һ�еĳ������
        				strline = strs.toCharArray();
        			}
        		}
        		i++;
            }
        	else if(ch == '/')
        	{
        		//����ע�Ͷ�ȡ�����ַ�	
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
			String strs = line_text[m];//ȡ��һ�еĳ������
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
        //�ж��Ƿ�Ϊ���
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
//        	System.out.println("��ӡ��Ϣ"+keywords.length+"   "+tmp+"    "+ token);
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
	
	
	//b��ָ����"֮�������
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
        	error_mes+=m+"���ַ�������δ��� "+tmp_token+"\n";
        }
        else{
        	pnext.add(string,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;	
        	error_mes+=m+"���ַ������� "+tmp_token+ch+"\n";
        }
		return i;
	}
	//���ַ��еĿո����ʹ��'\0'����ʾ�������ո������Ϊ����һ���������̵Ŀ�ʼ
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
        	error_mes+=m+"������δ��� "+tmp_token+"\n";
        	pnext.add(mychar,tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			
        }
        else if(length_flag)
        {
        	error_mes+=m+"���ַ����ȳ��� "+tmp_token+"\n";
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
        		error_mes+=m+"������ת����� "+tmp_token+"\n";
        		pnext.add("mychar",tmp_token);
        		pnext.pnext=new Token_list();
        		pnext=pnext.pnext;
			}
        	//System.out.println("state4:  "+tmp_token+"  "+search_zhuanyi(tmp_token));
        }
        else if(state==7)
        {
        	error_mes+=m+"����������� "+tmp_token+"\n";
        	pnext.add("mychar",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
        }
        else if(state==6)
        {
        	error_mes+=m+"�в���Ϊ���ַ� "+tmp_token+"\n";
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
		"########",//��Զ���ܵ����״̬
		"########",
		"#######a"}; 
	//�������ֵĳ���
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
            	error_mes+=m+"�г�����������\n";
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
        //System.out.println("��������ʱ��"+strline[i]);
        if(ch!=' '&&i!=strline.length&&!(isDigit(ch) || ch == '.' || ch == 'e' || ch == '-'))
        {
        	
        	while(ch!=' '&&ch!='\n'&&i<strline.length)
        	{
        		ch=strline[i];
        		tmp_token+=ch;
        		i++;
        	}
        	error_mes+=m+"�г�����������\n";
        	pnext.add("myfloat",tmp_token);
			pnext.pnext=new Token_list();
			pnext=pnext.pnext;
			
        	return i-2;
        }
        if(state==2||state==4||state==5)
    	{
    		//����
        	error_mes+=m+"�г�����������\n";
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
	
	
	//�ж��Ƿ��ǹؼ���
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
	
	//����ؼ��ֺͱ�ʶ��
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
		
		//����ǹؼ���
		int tmp_pos=isKeyword(tmp_token.toString());
		//System.out.println("Token�֣�"+tmp_token+"  "+tmp_pos);
		if (-1!=tmp_pos)//�����ǹؼ���   
        {  
			pnext.add(""+tmp_pos,"");
//			pnext.add(""+tmp_token, "-1");
			//System.out.println("����ģ�"+pnext.get());
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
	//��Ѱ��ʶ����λ��
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
	//����ķֲ�
	
	//ʶ�����ֵ��Զ���
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
	
	
	//�ж��Ƿ�Ϊ��ĸ���»���
	public static boolean isAlpha(char ch)
	{
	    return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
	}
	
	//�ж��Ƿ�������
	public static boolean isDigit(char ch)
	{  
        return (ch >= '0' && ch <= '9');  
    }
	//�ж��Ƿ�������� 
	public static boolean isOp(char ch) 
    {  
        for (int i = 0; i < operator.length; i++)  
            if (ch == operator[i]) {  
                return true;  
            }  
        return false;  
    }
	//���ļ�
	public static String readFile() throws IOException
	{
		Scanner in=new Scanner(System.in);
		//System.out.print("��ѡ���ļ���·����");
		//String str=in.nextLine();
		String str="D:\\demo.txt";
		FileReader fr=null;
		try {
			fr=new FileReader(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("��ȡ�ļ�����");
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

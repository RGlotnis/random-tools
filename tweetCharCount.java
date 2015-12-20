public static int getCount(String stringToCount, boolean includesImage) {
		//assuming utf-16 encoding
		//include local image
		int len = includesImage ? 24 : 0;
		//deal with URLs
		StringBuilder sb = new StringBuilder();
		/*Pattern pattern = Pattern.compile(""
			    + "(https?://)?[-\\w+&@#/%=~()|?!:,.;]*\\.[-\\w+&@#/%=~()|]*");*/
		
		//assuming that urls are separated with spaces/emojis
		//covers peculiar twitter URLs that end with a dot
		//note that twitter does not recognise all top level domains.  However,
		//it is likely that the customer URLs will be long enough for twitter to
		//automagically change them into t.co format. Hence, it should in practice
		//correspond to whatever char count twitter provides.
		Pattern pattern2 = Pattern.compile(""
			   + "(https?://)?[-\\w+&@#/%=~()|?!:,.;]*\\.[-\\w+&@#/%=~()|]+\\.?");		
		Matcher matcher = pattern2.matcher(stringToCount);
		int prevEnd = 0;
		while (matcher.find()) {
		    int matchStart = matcher.start(0);
		    int matchEnd = matcher.end();
		    if (matchStart>prevEnd) {
		    sb.append(stringToCount.substring(prevEnd,matchStart));
		    }
		    len +=23;
		    prevEnd = matchEnd;
		}
		sb.append(stringToCount.substring(prevEnd, stringToCount.length()));
		String text = sb.toString();
		//deal with emojis
		int i = 0;
		while (i<text.length()) {
			int tmp = (int)text.charAt(i);
			if (tmp<55296 || tmp>56319) {		//excluding UTF16 D800->DBFF
				i++;							//2byte char	
			}
			else {
				i+=2;							//4byte char
			}
			len++;
		}
		return len;
	}

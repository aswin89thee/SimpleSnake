package com.snake;

import java.io.*;
public class StatisticsInitializer
{
	public static final String SERIAL = "serial";

	public synchronized static void init(String[] args)
	{
		// Object serialization
		try
		{
			Statistics object1 = new Statistics();
			File file = new File(SERIAL);
			FileOutputStream fos = new FileOutputStream(SERIAL);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object1);
			oos.flush();
			oos.close();
		}
		catch(Exception e) {
			System.out.println("Exception during serialization: " + e);
			e.printStackTrace();
			System.exit(0);
		}

	}
}
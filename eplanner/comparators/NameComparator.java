/*
 *	Innehold: Generisk Comparatorklasse som kan brukes for å sammenlikne String- (evt. dato-) variabler for norske forhold.
 *
 *		Spesifikke metoder:
 *			compare				--> Sammenlikner de generiske objektenes navn evt. dato hvis objekter av typen <Concert>.
 *
 *
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 11.04.2012.
 */

package eplanner.comparators;

import eplanner.*;
import eplanner.utilities.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.text.*;

//	Hensikt: Sammenlikner strenger for bruk i sortering i TreeSet.

/*
 *  Krever implementering av NameComparable interfacet for mer robust kode slik at
 *  alle klasser som bruker denne comparatoren må implementere en getName metode.
 */

public class NameComparator<E extends NameComparable> implements Comparator<E>, Serializable
{
	private String order = "<\0<0<1<2<3<4<5<6<7<8<9<10<11<12<13<14<15<16<17<18<19<20<21<22<23<24<25<26<27<28<29<30" +
		"<A,a<B,b<C,c<D,d<E,e<F,f<G,g<H,h<I,i<J,j" +
		"<K,k<L,l<M,m<N,n<O,o<P,p<Q,q<R,r<S,s<T,t" +
		"<U,u<V,v<W,w<X,x<Y,y<Z,z<Æ,æ<Ø,ø<Å=AA,å=aa;AA,aa";

	private transient RuleBasedCollator collator;			//Denne variebelen en transient slik at man skal unngå NotSerializable exeption ved oppstart av programmet for andre gang.
	private transient boolean runOnce = true;
	private static final long serialVersionUID = 1337L;

	public NameComparator() // Konstruktør.
	{
		try
		{
			collator = new RuleBasedCollator(order);
		}
		catch(ParseException pe)
		{
			Utilities.showError(null,"Error on collator order!");
			IOActions.writeErrorLog(pe);
			System.exit(1);
		}
	}

	public int compare (E s1, E s2)// --> Sammenlikner de generiske objektenes navn evt. dato hvis objekter av typen <Concert>.
	{
		if(collator == null)
		{
			try{
				collator = new RuleBasedCollator(order);
			}
			catch(ParseException pe)
			{
				Utilities.showError(null,"Error on collator order!");
				IOActions.writeErrorLog(pe);
				System.exit(1);
			}
		}

		int c = 0;

		if(s1 == s2)
			return c;

		if(s1 instanceof Concert)
		{
			Concert dateS1 = (Concert) s1;
			Concert dateS2 = (Concert) s2;

			c = dateS1.getDate().compareTo(dateS2.getDate());

			if(c != 0)
				return c;
		}
		else if(s1 instanceof Receipt)
		{
			Receipt dateS1 = (Receipt) s1;
			Receipt dateS2 = (Receipt) s2;
			c = dateS1.getDate().compareTo(dateS2.getDate());

			if(c != 0)
				return c;
		}


		String n1 = s1.getName();
		String n2 = s2.getName();

		if(n1 == null) n1 = "";
		if(n2 == null) n2 = "";

		c = collator.compare(n1,n2);

		return c;
	}
}
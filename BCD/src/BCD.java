//BCD Project by Kai Chen
public class BCD {
	private int[] digits;
	private int[][] productLattice;
	
	BCD(int[] bcdDigits) {
		int bcdDigitIndex, digitIndex=0;
		digits = new int[bcdDigits.length];
		for (bcdDigitIndex=bcdDigits.length-1;
				bcdDigitIndex>-1; bcdDigitIndex--) {
			digits[digitIndex] = bcdDigits[bcdDigitIndex];
			digitIndex++;
		}
	}
	BCD(int num) {
		int digit = num % 10;
		num=num/10;
		digits = new int[1];
		digits[0] = digit;
		while (num>0) {
			digit = num % 10;
			addADigit(digit);
			num = num/10;
		}
	}
	
	public int numberOfDigits() {
		int digitNum = digits.length;
		return(digitNum);
	}
	public int nthDigit(int n) {
		int digitIndex;
		if ((n >= digits.length) || (n <= -1))
			digitIndex=0;
		else
			digitIndex=digits[digits.length-1-n];
		return(digitIndex);
	}
	public void addADigit(int newDigit) {
		int digitIndex, newDigitIndex=0;
		int[] newDigitArray = new int[digits.length+1];
		for (digitIndex=digits.length-1; digitIndex>-1; digitIndex--) {
			newDigitArray[newDigitIndex] = digits[digitIndex];
			newDigitIndex++;
		}
		newDigitArray[digits.length] = newDigit;
		newDigitIndex=0;
		digits = new int[newDigitArray.length];
		for (digitIndex=digits.length-1; 
				digitIndex>-1; digitIndex--) {
			digits[newDigitIndex] = newDigitArray[digitIndex];
			newDigitIndex++;
		}
	}
	public String toString() {
		int digitIndex, newDigitIndex=0, 
			commaNum, commaIndex, commaIteration=0, subStringIndex;
		int[] newDigitArray = new int[digits.length];
		String finalResult = "", tempResult = "";
		for (digitIndex=digits.length-1; digitIndex>-1; digitIndex--) {
			newDigitArray[newDigitIndex] = digits[digitIndex];
			newDigitIndex++;
		}
		if (digits.length%3==0) {
			commaNum=(digits.length/3)-1;
			for (commaNum=(digits.length/3)-1; commaNum>0; commaNum--) {
				for (commaIndex=commaIteration; commaIndex<3+commaIteration; 
					commaIndex++)
					finalResult = finalResult + newDigitArray[commaIndex];
				finalResult = finalResult + ",";
				commaIteration+=3;
			}
			commaNum = (digits.length/3)-1;
		}
		else {
			for (commaNum=digits.length/3; commaNum>0; commaNum--) {
				for (commaIndex=commaIteration; commaIndex<3+commaIteration; 
					commaIndex++) {
					finalResult = finalResult + newDigitArray[commaIndex];
				}
				finalResult = finalResult + ",";
				commaIteration+=3;
			}
			commaNum = digits.length/3;
		}
		for (newDigitIndex=(commaNum*3); newDigitIndex<newDigitArray.length; 
			newDigitIndex++)
			finalResult = finalResult + newDigitArray[newDigitIndex];
		for (subStringIndex=finalResult.length(); subStringIndex>0; 
			subStringIndex--)
			tempResult = tempResult + 
					finalResult.substring(subStringIndex-1,subStringIndex);
		finalResult = tempResult;
		return(finalResult);
	}
	public BCD addBCDs(BCD other) {
		int addIteration, totalIteration, remainder=0;
		if (this.numberOfDigits()>other.numberOfDigits())
			totalIteration=this.numberOfDigits();
		else
			totalIteration=other.numberOfDigits();
		int[] fillerNum = new int[0];
		BCD finalBCD = new BCD(fillerNum);
		for (addIteration=0; addIteration<totalIteration; addIteration++) {
			int sum = this.nthDigit(addIteration) + other.nthDigit(addIteration) + remainder;
			remainder = sum/10;
			int currentDigit = sum%10;
			finalBCD.addADigit(currentDigit);
		}
		if (remainder>0)
			finalBCD.addADigit(remainder);
		return(finalBCD);
	}
	public BCD multiplyBCDs (BCD other) {
		int rowIterate, columnIterate, iterate, remaining=0, sum=0, oneDigit;
		//Creating the lattice
		productLattice = new int[this.numberOfDigits()][other.numberOfDigits()];
		for (rowIterate=0; rowIterate<this.numberOfDigits(); rowIterate++) {
			for (columnIterate=0; columnIterate<other.numberOfDigits();columnIterate++) {
				productLattice[rowIterate][columnIterate]=
						this.nthDigit(rowIterate)*other.nthDigit(columnIterate);
			}
		}
		int[] diagonals = addDiagonals(productLattice);
		//Pseudo code - helped by Ivan E.
		sum = diagonals[0] + remaining;
		oneDigit = sum%10;
		remaining = sum/10;
		BCD answerBCD = new BCD(oneDigit);
		for (iterate=1; iterate<diagonals.length; iterate++) {
			sum = diagonals[iterate] + remaining;
			oneDigit = sum%10;
			remaining = sum/10;
			answerBCD.addADigit(oneDigit);
		}
		while (remaining>0) {
			oneDigit = remaining%10;
			remaining = remaining/10;
			answerBCD.addADigit(oneDigit);
		}
		if (answerBCD.nthDigit(answerBCD.numberOfDigits()-1)==0)
			answerBCD = new BCD(0);
		return answerBCD;
	}
	
	private int diagonalSum (int[][] lattice, int column, int row) {
		int sum=0;
		do {
			sum = lattice[row][column] + sum;
			column++;
			row--;
		} while ((column<lattice[0].length) && (row>=0));
		return sum;
	}
	private int[] addDiagonals(int[][] lattice){
		int height = lattice.length;
		int width = lattice[0].length;
		int diagonals = width+height-1;
		int[] result = new int[diagonals];
		int diag=diagonals-1;
		//Add the diagonals that end at the bottom
		for (int col=width-1;col>=0;col--) {
			result[diag]=diagonalSum(lattice,col,height-1);
			diag--;
		}
		//Add the diagonals that end at the left side
		for (int row = height-2; row>=0; row--) {
			result[diag]=diagonalSum(lattice,0,row);
			diag--;
		}
		return result;
	}
	public static BCD factorial(int num) {
		BCD firstBCD = new BCD(1);
		BCD secondBCD;
		int count;
		for (count=2; count<=num; count++) {
			secondBCD = new BCD(count);
			firstBCD = firstBCD.multiplyBCDs(secondBCD);
		}
		return firstBCD;
	}
	public BCD pow(int num) {
		BCD finalBCD = new BCD(1);
		int count;
		if (num==0)
			finalBCD = new BCD(1);
		else
			for (count=1; count<=num; count++)
				finalBCD = finalBCD.multiplyBCDs(this);
		return finalBCD;
	}
	
	//Driver Method
	public static void main(String str[]) {
		//BCD-6 Test Drive
		//Test 1 - 52 factorial
		System.out.println(factorial(52));
		//Test 2 - 104 Factorial
		System.out.println(factorial(104));
		//Test 3 - 2 raised to the 127th power
		BCD thirdTest = new BCD(2);
		System.out.println(thirdTest.pow(127));
		//Test 4 - 19 raised to the 19th power
		BCD fourthTest = new BCD(19);
		System.out.println(fourthTest.pow(19));
		
		/*
		//BCD-5 Test Drive
		//Test 1 - both at least three digits
		BCD firstTest1 = new BCD(329);
		BCD firstTest2 = new BCD(4857);
		System.out.println(firstTest1.multiplyBCDs(firstTest2));
		
		//Test 2 - this = one digit, other = one digit + 1
		BCD secondTest1 = new BCD(9);
		BCD secondTest2 = new BCD(15);
		System.out.println(secondTest1.multiplyBCDs(secondTest2));
		
		//Test 3 - both with one digit
		BCD thirdTest1 = new BCD(4);
		BCD thirdTest2 = new BCD(9);
		System.out.println(thirdTest1.multiplyBCDs(thirdTest2));
		
		//Test 4 - this = 0, other = whatever
		BCD fourthTest1 = new BCD(0);
		BCD fourthTest2 = new BCD(1204);
		System.out.println(fourthTest1.multiplyBCDs(fourthTest2));
		
		//Test 5 - Two long BCDs, both contain at least 1 zero
		BCD fifthTest1 = new BCD(52058);
		BCD fifthTest2 = new BCD(1280501);
		System.out.println(fifthTest1.multiplyBCDs(fifthTest2));
		
		//Test 6 - Two long BCDs, this is divisible by 10, other is not
		BCD sixthTest1 = new BCD(91240);
		BCD sixthTest2 = new BCD(512023);
		System.out.println(sixthTest1.multiplyBCDs(sixthTest2));
		*/
		/*
		//BCD-4 Test Drive
		//Test 1 - both = 0
		BCD firstTest1 = new BCD(0);
		BCD firstTest2 = new BCD(0);
		System.out.println(firstTest1.addBCDs(firstTest2));
		
		//Test 2 - first = 0, other =/= 0
		BCD secondTest1 = new BCD(0);
		BCD secondTest2 = new BCD(8);
		System.out.println(secondTest1.addBCDs(secondTest2));
		
		//Test 3 - first =/= 0, other = 0
		BCD thirdTest1 = new BCD(10);
		BCD thirdTest2 = new BCD(0);
		System.out.println(thirdTest1.addBCDs(thirdTest2));
		
		//Test 4 - No carry, first < other
		BCD fourthTest1 = new BCD(7);
		BCD fourthTest2 = new BCD(11);
		System.out.println(fourthTest1.addBCDs(fourthTest2));
		
		//Test 5 - No carry, first > other
		BCD fifthTest1 = new BCD(16);
		BCD fifthTest2 = new BCD(3);
		System.out.println(fifthTest1.addBCDs(fifthTest2));
		
		//Test 6 - Carry, first > other, and result > other
		BCD sixthTest1 = new BCD(19);
		BCD sixthTest2 = new BCD(7);
		System.out.println(sixthTest1.addBCDs(sixthTest2));
		
		//Test 7 - Carry, first < other, and result > first
		BCD seventhTest1 = new BCD(9);
		BCD seventhTest2 = new BCD(16);
		System.out.println(seventhTest1.addBCDs(seventhTest2));
		
		//Test 8 - There is more than one carry
		BCD eighthTest1 = new BCD(99999);
		BCD eighthTest2 = new BCD(5);
		System.out.println(eighthTest1.addBCDs(eighthTest2));
		*/
		/*
		//BCD-3 Test Drive
		//Test 1
		BCD myFirstBCD = new BCD(0);
		System.out.println(myFirstBCD.toString());
		
		//Test 2
		BCD mySecondBCD = new BCD(8);
		System.out.println(mySecondBCD.toString());
		
		//Test 3
		BCD myThirdBCD = new BCD(942);
		System.out.println(myThirdBCD.toString());
		
		//Test 4
		BCD myFourthBCD = new BCD(1082);
		System.out.println(myFourthBCD.toString());
		
		//Test 5
		BCD myFifthBCD = new BCD(802341);
		System.out.println(myFifthBCD.toString());
		
		//Test 6
		BCD mySixthBCD = new BCD(654029380);
		System.out.println(mySixthBCD.toString());
		*/
		/*
		//BCD 2 Test Drive
		int digitIndex;
		//Test 1
		int[] firstArray = {0};
		BCD myFirstBCD;
		myFirstBCD = new BCD(firstArray);
		System.out.println("//First Test");
		System.out.println("Digit right before the end digit doesn't exist.");
		System.out.println("End digit is: " + myFirstBCD.nthDigit(0));
		System.out.println("Digit right after end digit is: " 
			+ myFirstBCD.nthDigit(1));
		System.out.println("Number of digit(s) is: " 
			+ myFirstBCD.numberOfDigits());
		System.out.print("Adding another digit holding 7 yields: ");
		myFirstBCD.addADigit(7);
		for (digitIndex=0; digitIndex<myFirstBCD.digits.length; digitIndex++)
			System.out.print(myFirstBCD.digits[digitIndex]);
		System.out.println();
		
		//Test 2
		int[] secondArray = {7};
		BCD mySecondBCD;
		mySecondBCD = new BCD(secondArray);
		System.out.println("//Second Test");
		System.out.println("Digit right before the end digit doesn't exist.");
		System.out.println("End digit is: " + mySecondBCD.nthDigit(0));
		System.out.println("Digit right after end digit is: " 
			+ mySecondBCD.nthDigit(1));
		System.out.println("Number of digit(s) is: " 
			+ mySecondBCD.numberOfDigits());
		System.out.print("Adding another digit holding 4 yields: ");
		mySecondBCD.addADigit(4);
		for (digitIndex=0; digitIndex<mySecondBCD.digits.length; digitIndex++)
			System.out.print(mySecondBCD.digits[digitIndex]);
		System.out.println();
		
		//Test 3
		int[] thirdArray = {1,7};
		BCD myThirdBCD;
		myThirdBCD = new BCD(thirdArray);
		System.out.println("//Third Test");
		System.out.println("Digit right before the end digit is: "
			+ myThirdBCD.nthDigit(0));
		System.out.println("End digit is: " + myThirdBCD.nthDigit(1));
		System.out.println("Digit right after end digit is: " 
			+ myThirdBCD.nthDigit(2));
		System.out.println("Number of digit(s) is: " 
			+ myThirdBCD.numberOfDigits());
		System.out.print("Adding another digit holding 1 yields: ");
		myThirdBCD.addADigit(1);
		for (digitIndex=0; digitIndex<myThirdBCD.digits.length; digitIndex++)
			System.out.print(myThirdBCD.digits[digitIndex]);
		System.out.println();
		
		//Test 4
		int[] fourthArray = {1,7,4,6,8,2};
		BCD myFourthBCD;
		myFourthBCD = new BCD(fourthArray);
		System.out.println("//Fourth Test");
		System.out.println("Digit right before the end digit is: "
			+ myFourthBCD.nthDigit(4));
		System.out.println("End digit is: " + myFourthBCD.nthDigit(5));
		System.out.println("Digit right after end digit is: " 
			+ myFourthBCD.nthDigit(6));
		System.out.println("Number of digit(s) is: " 
			+ myFourthBCD.numberOfDigits());
		System.out.print("Adding another digit holding 6 yields: ");
		myFourthBCD.addADigit(6);
		for (digitIndex=0; digitIndex<myFourthBCD.digits.length; digitIndex++)
			System.out.print(myFourthBCD.digits[digitIndex]);
		System.out.println();
		*/
	}
}
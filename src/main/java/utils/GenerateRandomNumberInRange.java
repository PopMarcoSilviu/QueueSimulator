package utils;

public interface GenerateRandomNumberInRange
{
     static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

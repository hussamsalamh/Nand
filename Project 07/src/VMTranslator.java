import java.util.Arrays;

/**
 * Created by Era on 19/04/2016.
 */
public class VMTranslator
{
    public static void main(String[] args)
    {
        System.out.println(Arrays.toString("pop    d".trim().replaceAll(" +", " ").split(" ")));
    }
}

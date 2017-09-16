package quadTree;

public class Util {

	//判断用户是否在该区域
	public static boolean isUserInside(double[] location, double[] rect)
	{
		if(location[0]>=rect[0] && location[0]<=rect[2]
				&& location[1]>=rect[1] && location[1]<=rect[3]){
			return true;
		}
		else
			return false;
	}
	//判断区域A是否在区域B中
	public static boolean isInsideRect(double[] rectA, double[] rectB)
	{   
		if(rectA[0]>=rectB[0] && rectA[2]<=rectB[2] 
				&& rectA[1]>=rectB[1] && rectA[3]<=rectB[3]){
			return true;
		}
        else {
            return false;  
        }      
    }
	//判断区域A是否覆盖区域B
    public static boolean isOverLapped(double[] rectA, double[] rectB)
    {  
        if(rectA[0]<=rectB[0] && rectA[2]>=rectB[2]
        		&& rectA[1]<=rectB[1] && rectA[3]>=rectB[3]){  
            return true;  
        }
        else{  
            return false;  
        }  
    }
}

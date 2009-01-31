#include "math.h"

const double theGreekMathPie =  3.14159265358979;

double degreesToRadians(double deg) {
	return (((180.0)*deg)/(theGreekMathPie));
}


double radiansToDegrees(double rad) {
	return (((rad)*(theGreekMathPie))/(180.0));
}


// Include the libraries we need
#include <OneWire.h>
#include <DallasTemperature.h>
#include <MultiFuncShield.h>
#include <TimerOne.h>
#define CMDBUFFER_SIZE 32

// Data wire is plugged into port A4 on the Arduino
#define ONE_WIRE_BUS A4

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

//int power = 1;
int lamp = 1;
const int powerPin = 10; // pin the LED is connected to
int blinkRate=0;     // blink rate stored in this variable
String a;
float previous = 0;
float measure = 0;
float target = 0;
String power;
String id = "ARD123";
String sep = "/";

/*
 * The setup function. We only start the sensors here
 */
void setup(void)
{
  Timer1.initialize();
  MFS.initialize(&Timer1);    // initialize multi-function shield library
  Serial.begin(9600);
  pinMode(powerPin, OUTPUT); // set this pin as output
  sensors.begin();
  MFS.write("OFF");

}
/*
 * Main function, get and show the temperature
 */
void loop() {    
    measure = getMeasure();
    if(measure < target){
        digitalWrite(powerPin,LOW);
        power = "ON";
      }
      else{
        digitalWrite(powerPin,HIGH);
        power = "OFF";
      }
    if (previous != measure){
//      Serial.println(measure, 1);
      Serial.println(id + sep + power+ sep + measure);
      MFS.write(measure, 2);
      previous = measure;
    } 
}

void serialEvent()
{
 static char cmdBuffer[CMDBUFFER_SIZE] = "";
 char c;
 while(Serial.available()) 
 {
   c = processCharInput(cmdBuffer, Serial.read());
//   Serial.print(c);
   if (c == '\n') 
   {
//      Serial.println();
     //Full command received. Do your stuff here!
  
      if (atof(cmdBuffer)){
        Serial.println(id + sep + "SET"+ sep + cmdBuffer);
        target = atof(cmdBuffer);
        MFS.write("SET");
      }
          
//     }
     if (strcmp("ON", cmdBuffer) == 0)
     {
        MFS.write("ON");
        digitalWrite(powerPin,LOW);
     }
     if (strcmp("OFF", cmdBuffer) == 0)
     {
        MFS.write("OFF");
        digitalWrite(powerPin,HIGH);
     }
     cmdBuffer[0] = 0;
   }
 }
 delay(1);
}

char processCharInput(char* cmdBuffer, const char c)
{
 //Store the character in the input buffer
 if (c >= 32 && c <= 126) //Ignore control characters and special ascii characters
 {
   if (strlen(cmdBuffer) < CMDBUFFER_SIZE) 
   { 
     strncat(cmdBuffer, &c, 1);   //Add it to the buffer
   }
   else  
   {   
     return '\n';
   }
 }
 else if ((c == 8 || c == 127) && cmdBuffer[0] != 0) //Backspace
 {

   cmdBuffer[strlen(cmdBuffer)-1] = 0;
 }

 return c;
}

float getMeasure(){
  sensors.requestTemperatures(); // Send the command to get temperatures
  float measure = sensors.getTempCByIndex(0);
  measure = measure*10.0f;
  measure = (measure > (floor(measure)+0.5f)) ? ceil(measure) : floor(measure);
  measure = measure/10.0f;
  return measure;
}

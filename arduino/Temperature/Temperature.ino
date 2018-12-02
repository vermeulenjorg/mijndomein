// Include the libraries we need
#include <OneWire.h>
#include <DallasTemperature.h>
#include <MultiFuncShield.h>
#include <TimerOne.h>

// Data wire is plugged into port A4 on the Arduino
#define ONE_WIRE_BUS A4

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

const int ledPin = 10; // pin the LED is connected to
int   blinkRate=0;     // blink rate stored in this variable

/*
 * The setup function. We only start the sensors here
 */
void setup(void)
{
  Timer1.initialize();
  MFS.initialize(&Timer1);    // initialize multi-function shield library
//  MFS.write("Hi",0); // links uitlijnen
  // start serial port
  Serial.begin(9600);
    pinMode(ledPin, OUTPUT); // set this pin as output
//  Serial.println("Dallas Temperature IC Control Library Demo");

  // Start up the library
  sensors.begin();
  MFS.write("OFF");
}

/*
 * Main function, get and show the temperature
 */
void loop(void)
{ 
  sensors.requestTemperatures(); // Send the command to get temperatures
  float temp = sensors.getTempCByIndex(0);
  delay(2000);
  if ( Serial.available()) // Check to see if at least one character is available
  {
    char ch = Serial.read();
    if(ch == '1') // is this an ascii digit between 0 and 1?
    {
       digitalWrite(ledPin,LOW);
       showTemp(temp);
//       blinkRate = (ch - '0');      // ASCII value converted to numeric value
//       blinkRate = (blinkRate + 5) * 100; // actual blinkrate is 100 mS times received digit
      
//       Serial.write(ch);
//       Serial.write(ch);
    }
    if(ch == '0') // is this an ascii digit between 0 and 1?
    {
//      digitalWrite(ledPin,LOW);
      digitalWrite(ledPin,HIGH);
      MFS.write("OFF");
     }
  }
//  blink();
}

void showTemp(float temp)
{
  Serial.println(temp);
  MFS.write(temp, 2);
}
void blink()
{
  digitalWrite(ledPin,LOW);
  delay(blinkRate); // delay depends on blinkrate value
  digitalWrite(ledPin,HIGH);
  delay(blinkRate);
}

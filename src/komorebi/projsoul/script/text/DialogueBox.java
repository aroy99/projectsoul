package komorebi.projsoul.script.text;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;

public class DialogueBox {

  private SpeechHandler speaker;

  private int pickIndex;
  private int maxOpt;

  private boolean saying, asking;

  private TrackableThread letFinish;
  private TrackableThread waitingOnDialogue;
  
  private boolean unfinished;

  public void setSpeaker(SpeechHandler speaker)
  {    
    setSpeechHandler(speaker);
    saying = true;
    lockThread();
  }

  public void setAsker(SpeechHandler speaker)
  {    
    setSpeechHandler(speaker);
    speaker.setAskMode(true);
    asking = true;

    pickIndex = 1;
    maxOpt = speaker.maxOptionIndex();
    lockThread();
  }

  private void setSpeechHandler(SpeechHandler speaker)
  {        
    if (unfinished)
      letFinish();
    
    this.speaker = speaker;
    unfinished = true;
  }

  public boolean hasDialogue()
  {
    return saying || asking;
  }

  public boolean hasQuestion()
  {
    return asking;
  }

  public void render()
  {    
    speaker.render();

    if (asking && speaker.alreadyAsked())
    {
      int x = 0, y= 0;
      switch (pickIndex)
      {
        case 1: x = 20; y = 40; break;
        case 2: x = 90; y = 40; break;
        case 3: x = 20; y = 22; break;
        case 4: x = 90; y = 22; break;
        default: break;
      }

      Draw.rect(x, y, 8, 8, 0, 0, 8, 8, 7); 
      //Draws the "picker" arrow

    }

  }

  public void movePickerLeft()
  {
    increment(-1);
  }

  public void movePickerRight()
  {
    increment(1);
  }

  private void increment(int increment)
  {
    pickIndex += increment;

    if (pickIndex < 1)
    {
      pickIndex = maxOpt;
    }

    if (pickIndex > maxOpt)
    {
      pickIndex = 1;
    }
  }

  public void next()
  {
    if (speaker.isWaitingOnParagraph())
    {
      speaker.nextParagraph();
    } else {
      if (!speaker.alreadyAsked())
      {
        speaker.skipScroll();
      } else
      {
        if (hasDialogue())
        {
          finish();
        }
      }
    }
  }

  private void finish()
  {    
    speaker.clear();

    if (asking) {
      speaker.branch(pickIndex);
      speaker.setAskMode(false);
    }

    waitingOnDialogue.unlock();


    if (speaker instanceof SignHandler)
    {
      SignHandler sign = (SignHandler) speaker;
      sign.disengage();
    }

    speaker = null;
    saying=false;
    asking=false;
    
    unfinished = false;

    if (hasThreadWaiting())
    {
      letThreadContinue();
    }
  }

  public void lockThread()
  {
    waitingOnDialogue = ThreadHandler.currentThread();
    ThreadHandler.lockCurrentThread();
  }

  public void unlockWaitingThread()
  {

  }

  public void letFinish()
  {
    letFinish = ThreadHandler.currentThread();
    ThreadHandler.lockCurrentThread();
  }

  private boolean hasThreadWaiting()
  {
    return letFinish != null;
  }

  private void letThreadContinue()
  {
    letFinish.unlock();
    letFinish = null;
  }
}

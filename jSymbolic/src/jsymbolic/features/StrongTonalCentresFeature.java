/*
 * StrongTonalCentresFeature.java
 * Version 1.2
 *
 * Last modified on April 11, 2010.
 * McGill University
 */

package jsymbolic.features;

import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic.processing.MIDIIntermediateRepresentations;


/**
 * A feature exractor that finds the number of peaks in the fifths pitch 
 * histogram that each account for at least 9% of all Note Ons.
 *
 * <p>No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class StrongTonalCentresFeature
     extends MIDIFeatureExtractor
{
     /* CONSTRUCTOR ***********************************************************/
     
     
     /**
      * Basic constructor that sets the definition and dependencies (and their
      * offsets) of this feature.
      */
     public StrongTonalCentresFeature()
     {
          String name = "Strong Tonal Centres";
          String description = "Number of peaks in the fifths pitch histogram that each account\n" +
               "for at least 9% of all Note Ons.";
          boolean is_sequential = true;
          int dimensions = 1;
          definition = new FeatureDefinition( name,
               description,
               is_sequential,
               dimensions );
          
          dependencies = null;
          
          offsets = null;
     }
     
     
     /* PUBLIC METHODS ********************************************************/
     
     
     /**
      * Extracts this feature from the given MIDI sequence given the other
      * feature values.
      *
      * <p>In the case of this feature, the other_feature_values parameters
      * are ignored.
      *
      * @param sequence			The MIDI sequence to extract the feature
      *                                 from.
      * @param sequence_info		Additional data about the MIDI sequence.
      * @param other_feature_values	The values of other features that are
      *					needed to calculate this value. The
      *					order and offsets of these features
      *					must be the same as those returned by
      *					this class's getDependencies and
      *					getDependencyOffsets methods
      *                                 respectively. The first indice indicates
      *                                 the feature/window and the second
      *                                 indicates the value.
      * @return				The extracted feature value(s).
      * @throws Exception		Throws an informative exception if the
      *					feature cannot be calculated.
      */
     public double[] extractFeature( Sequence sequence,
          MIDIIntermediateRepresentations sequence_info,
          double[][] other_feature_values )
          throws Exception
     {
          double value;
          if (sequence_info != null)
          {
               // Check all peaks
               int peaks = 0;
               for (int bin = 0; bin < sequence_info.fifths_pitch_histogram.length; bin++)
               {
                    if (sequence_info.fifths_pitch_histogram[bin] >= 0.09)
                    {
                         int left = bin - 1;;
                         int right = bin + 1;
                         
                         // Account for wrap around
                         if (right == sequence_info.fifths_pitch_histogram.length)
                              right = 0;
                         if (left == -1)
                              left = sequence_info.fifths_pitch_histogram.length - 1;
                         
                         // Check if is a peak
                         if ( sequence_info.fifths_pitch_histogram[bin] > sequence_info.fifths_pitch_histogram[left] &&
                              sequence_info.fifths_pitch_histogram[bin] > sequence_info.fifths_pitch_histogram[right] )
                              peaks++;
                    }
               }
               
               // Calculate the value
               value = (double) peaks;
          }
          else value = -1.0;
          
          double[] result = new double[1];
          result[0] = value;
          return result;
     }
}
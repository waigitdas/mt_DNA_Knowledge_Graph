/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

public class seq_probe {
    int ref_start_pos;
    int start_pos;
    int end_pos;
    int length;
    String ref_probe;
    String test_probe;
    int method;
    String left_flank;
    String right_flank;
    int frameshift;
    String diff;
    String cq;
    String error;

public int get_ref_start_pos()
{
    return this.ref_start_pos;
}

public String get_ref_probe()
{
    return this.ref_probe ;
}

public String get_test_probe()
{
    return this.test_probe;
}
    
public int get_start_pos()
{
    return this.start_pos;
}

public  int get_end_pos()
{
    return this.end_pos;
}

public int get_length()
{
    return this.length;
}

public String get_cq()
{
    return this.cq;
}

public int get_method()
{
    return this.method;
}

public String get_left_flank()
{
    return this.left_flank;
}

public String get_right_flank()
{
    return this.right_flank;
}

public int get_frameshift()
{
    return this.frameshift;
}

public String get_diff()
{
    return this.diff;
}

public String get_error()
{
    return this.error;
}

}


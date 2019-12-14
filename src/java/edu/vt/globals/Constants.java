
/*
 * Created by Justin Kennedy on 2019.11.16  * 
 * Copyright © 2019 Justin Kennedy. All rights reserved. * 
 */
package edu.vt.globals;

public final class Constants {

    /*
    ==================================================
    |   Use of Class Variables as Global Constants   |
    ==================================================
    
    All of the variables in this class are Class Variables (typed with the "static" keyword")
    with Constant values, which can be accessed in any class in the project by specifying
    Constants.CONSTANTNAME, i.e., ClassName.ClassVariableName
    
    ==================================================
    |             Our Design Decision                |
    ==================================================
    
    We decided to use directories external to our application for the storage and retrieval of user's files.
    We do not want to use a database for the following reasons: 
    
        (a) Database storage and retrieval of large files as BLOB (Binary Large OBject) degrades performance.
        (b) BLOBs increase the database complexity.
        (c) The operating system handles the external files instead of the database management system.
    
    Therefore, we use an alternate document root (DocRoot) for the storage and retrieval of user's files.
    
    ==================================================
    |             Absolute File Paths                |
    ==================================================

    Windows OS: Specify file or directory paths starting with C:
    
    public static final String PHOTOS_ABSOLUTE_PATH = "C:/Users/Balci/DocRoot/UserPhotoStorage/";
    
    macOS (Unix or Linux): Specify the absolute directory path as shown below.
    - Balci
    dir=/home/cloudsd/Kennedy/DocRoot
     */
    public static final String PHOTOS_ABSOLUTE_PATH = "C:/Users/Yogi/DocRoot/VTRidesStorage/";

    /*
    In glassfish-web.xml, we specified an alternate document root (DocRoot) with the statement below
    to allow our application to read and write files external to (outside of) its deployed directory.

        <property name="alternatedocroot_1" value="from=/UserPhotoStorage/* dir=/Users/Balci/DocRoot" />

    dir=    specifies the DocRoot directory's absolute path where the external files are stored.
    from=   /UserPhotoStorage/* specifies the top directory in the DocRoot directory.
    *       implies that you can put directories and files within the /UserPhotoStorage/ top directory.

    NOTE:   Pay attention to the slashes! Different parts will be combined to form a file path specification,
            Example: /Users/Balci/DocRoot/UserPhotoStorage/defaultUserPhoto.png

    For ease of change (maintainability), we use Relative Paths to access the external files.
    
    ==================================================
    |        How Does the Relative Path Work?        |
    ==================================================
    
    When the JSF page with a relative path to an external file is rendered, JSF HTML tag library
    tag <h> automatically inserts the app name, e.g., /BevQ, before the relative path.
    Then, for example, when
    
        /BevQ/UserPhotoStorage/defaultUserPhoto.png
    
    is processed by GlassFish, GlassFish knows because of the glassfish-web.xml file that the
    DocRoot is "/Users/Balci/DocRoot" for the app named BevQ and replaces the app name with
    DocRoot to obtain the absolute path to the file, 
    
        /Users/Balci/DocRoot/UserPhotoStorage/defaultUserPhoto.png
    
    GlassFish has to determine the absolute path by using the relative path. 
    Inserting the absolute path directly in the JSF page does not work!
    
    However, some HTML5 tags do not automatically insert the app name before a relative path
    such as the <iframe> and <video> tags. Therefore, you need to insert it yourself as shown below.
    
    <iframe src="#{facesContext.externalContext.requestContextPath}#{userFileController.selectedFileRelativePath()}" 
                                    width="600" height="400" scrolling="yes" />
    
    <video width="512" height="288" preload="auto" controls="controls" 
        src="#{facesContext.externalContext.requestContextPath}#{userFileController.selectedFileRelativePath()}" />
     
    #{facesContext.externalContext.requestContextPath} returns 
        <> the app name starting with slash, e.g., /BevQ, if an external file is referenced or
        <> the URI of the Web Pages folder of the application if an internal file is referenced.
    - Balci
     */
    public static final String PHOTOS_RELATIVE_PATH = "/VTRidesStorage/";
    public static final String DEFAULT_PHOTO_RELATIVE_PATH = "/VTRidesStorage/defaultUserPhoto.png";

    /* 
    ==================================================
    |             Our Design Decision                |
    ==================================================
    
    We decided to scale down the user's uploaded photo to 200x200 px,
    which we call the Thumbnail photo, and use it.
    
    We do not want to use the uploaded photo as is, which may be
    very large in size degrading performance.
     */
    public static final Integer THUMBNAIL_SIZE = 200;

    /* 
    United States postal state abbreviations (codes) These are accessed by the userController
     */
    public static final String[] STATES = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT",
        "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA",
        "MD", "ME", "MH", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
        "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT",
        "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

    /* 
    A security question is selected and answered by the user at the time of account creation.
    The selected question is used as a second level of authentication in addition to password.
    These are accessed by the userController
     */
    public static final String[] QUESTIONS = {
        "In what city or town did your mother and father meet?",
        "In what city or town were you born?",
        "What did you want to be when you grew up?",
        "What do you remember most from your childhood?",
        "What is the name of the boy or girl that you first kissed?",
        "What is the name of the first school you attended?",
        "What is the name of your favorite childhood friend?",
        "What is the name of your first pet?",
        "What is your mother's maiden name?",
        "What was your favorite place to visit as a child?"
    };
      
}

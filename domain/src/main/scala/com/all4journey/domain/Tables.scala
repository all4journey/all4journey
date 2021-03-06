package com.all4journey.domain

// $COVERAGE-OFF$
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Address.schema, SchemaVersion.schema, Token.schema, User.schema, UsState.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Address
    *  @param id Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true)
    *  @param userId Database column USER_ID SqlType(VARCHAR), Length(50,true)
    *  @param street Database column STREET SqlType(VARCHAR), Length(100,true), Default(None)
    *  @param stateId Database column STATE_ID SqlType(VARCHAR), Length(2,true)
    *  @param zipcode Database column ZIPCODE SqlType(VARCHAR), Length(10,true)
    *  @param addressType Database column ADDRESS_TYPE SqlType(VARCHAR), Length(10,true)
    *  @param placeName Database column PLACE_NAME SqlType(VARCHAR), Length(10,true) */
  case class AddressRow(id: String, userId: String, street: Option[String] = None, stateId: String, zipcode: String, addressType: String, placeName: String)
  /** GetResult implicit for fetching AddressRow objects using plain SQL queries */
  implicit def GetResultAddressRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[AddressRow] = GR{
    prs => import prs._
      AddressRow.tupled((<<[String], <<[String], <<?[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table ADDRESS. Objects of this class serve as prototypes for rows in queries. */
  class Address(_tableTag: Tag) extends Table[AddressRow](_tableTag, "ADDRESS") {
    def * = (id, userId, street, stateId, zipcode, addressType, placeName) <> (AddressRow.tupled, AddressRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), street, Rep.Some(stateId), Rep.Some(zipcode), Rep.Some(addressType), Rep.Some(placeName)).shaped.<>({r=>import r._; _1.map(_=> AddressRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val id: Rep[String] = column[String]("ID", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column USER_ID SqlType(VARCHAR), Length(50,true) */
    val userId: Rep[String] = column[String]("USER_ID", O.Length(50,varying=true))
    /** Database column STREET SqlType(VARCHAR), Length(100,true), Default(None) */
    val street: Rep[Option[String]] = column[Option[String]]("STREET", O.Length(100,varying=true), O.Default(None))
    /** Database column STATE_ID SqlType(VARCHAR), Length(2,true) */
    val stateId: Rep[String] = column[String]("STATE_ID", O.Length(2,varying=true))
    /** Database column ZIPCODE SqlType(VARCHAR), Length(10,true) */
    val zipcode: Rep[String] = column[String]("ZIPCODE", O.Length(10,varying=true))
    /** Database column ADDRESS_TYPE SqlType(VARCHAR), Length(10,true) */
    val addressType: Rep[String] = column[String]("ADDRESS_TYPE", O.Length(10,varying=true))
    /** Database column PLACE_NAME SqlType(VARCHAR), Length(10,true) */
    val placeName: Rep[String] = column[String]("PLACE_NAME", O.Length(10,varying=true))

    /** Foreign key referencing AddressType (database name ADDRESS_ibfk_3) */
    lazy val addressTypeFk = foreignKey("ADDRESS_ibfk_3", addressType, AddressType)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing User (database name ADDRESS_ibfk_1) */
    lazy val userFk = foreignKey("ADDRESS_ibfk_1", userId, User)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing UsState (database name ADDRESS_ibfk_2) */
    lazy val usStateFk = foreignKey("ADDRESS_ibfk_2", stateId, UsState)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Address */
  lazy val Address = new TableQuery(tag => new Address(tag))

  /** Entity class storing rows of table AddressType
    *  @param id Database column ID SqlType(VARCHAR), PrimaryKey, Length(10,true)
    *  @param description Database column DESCRIPTION SqlType(VARCHAR), Length(50,true) */
  case class AddressTypeRow(id: String, description: String)
  /** GetResult implicit for fetching AddressTypeRow objects using plain SQL queries */
  implicit def GetResultAddressTypeRow(implicit e0: GR[String]): GR[AddressTypeRow] = GR{
    prs => import prs._
      AddressTypeRow.tupled((<<[String], <<[String]))
  }
  /** Table description of table ADDRESS_TYPE. Objects of this class serve as prototypes for rows in queries. */
  class AddressType(_tableTag: Tag) extends Table[AddressTypeRow](_tableTag, "ADDRESS_TYPE") {
    def * = (id, description) <> (AddressTypeRow.tupled, AddressTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(description)).shaped.<>({r=>import r._; _1.map(_=> AddressTypeRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), PrimaryKey, Length(10,true) */
    val id: Rep[String] = column[String]("ID", O.PrimaryKey, O.Length(10,varying=true))
    /** Database column DESCRIPTION SqlType(VARCHAR), Length(50,true) */
    val description: Rep[String] = column[String]("DESCRIPTION", O.Length(50,varying=true))
  }
  /** Collection-like TableQuery object for table AddressType */
  lazy val AddressType = new TableQuery(tag => new AddressType(tag))

  /** Entity class storing rows of table SchemaVersion
    *  @param versionRank Database column version_rank SqlType(INT)
    *  @param installedRank Database column installed_rank SqlType(INT)
    *  @param version Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true)
    *  @param description Database column description SqlType(VARCHAR), Length(200,true)
    *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
    *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
    *  @param checksum Database column checksum SqlType(INT), Default(None)
    *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
    *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
    *  @param executionTime Database column execution_time SqlType(INT)
    *  @param success Database column success SqlType(BIT) */
  case class SchemaVersionRow(versionRank: Int, installedRank: Int, version: String, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[java.sql.Timestamp], e4: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
      SchemaVersionRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
    *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends Table[SchemaVersionRow](_tableTag, "schema_version") {
    def * = (versionRank, installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(versionRank), Rep.Some(installedRank), Rep.Some(version), Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column version_rank SqlType(INT) */
    val versionRank: Rep[Int] = column[Int]("version_rank")
    /** Database column installed_rank SqlType(INT) */
    val installedRank: Rep[Int] = column[Int]("installed_rank")
    /** Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val version: Rep[String] = column[String]("version", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
      *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INT), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INT) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BIT) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (installedRank) (database name schema_version_ir_idx) */
    val index1 = index("schema_version_ir_idx", installedRank)
    /** Index over (success) (database name schema_version_s_idx) */
    val index2 = index("schema_version_s_idx", success)
    /** Index over (versionRank) (database name schema_version_vr_idx) */
    val index3 = index("schema_version_vr_idx", versionRank)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))

  /** Entity class storing rows of table Token
    *  @param id Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true)
    *  @param userId Database column USER_ID SqlType(VARCHAR), Length(50,true)
    *  @param token Database column TOKEN SqlType(VARCHAR), Length(50,true) */
  case class TokenRow(id: String, userId: String, token: String)
  /** GetResult implicit for fetching TokenRow objects using plain SQL queries */
  implicit def GetResultTokenRow(implicit e0: GR[String]): GR[TokenRow] = GR{
    prs => import prs._
      TokenRow.tupled((<<[String], <<[String], <<[String]))
  }
  /** Table description of table TOKEN. Objects of this class serve as prototypes for rows in queries. */
  class Token(_tableTag: Tag) extends Table[TokenRow](_tableTag, "TOKEN") {
    def * = (id, userId, token) <> (TokenRow.tupled, TokenRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(token)).shaped.<>({r=>import r._; _1.map(_=> TokenRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val id: Rep[String] = column[String]("ID", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column USER_ID SqlType(VARCHAR), Length(50,true) */
    val userId: Rep[String] = column[String]("USER_ID", O.Length(50,varying=true))
    /** Database column TOKEN SqlType(VARCHAR), Length(50,true) */
    val token: Rep[String] = column[String]("TOKEN", O.Length(50,varying=true))

    /** Foreign key referencing User (database name TOKEN_ibfk_1) */
    lazy val userFk = foreignKey("TOKEN_ibfk_1", userId, User)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Token */
  lazy val Token = new TableQuery(tag => new Token(tag))

  /** Entity class storing rows of table User
    *  @param id Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true)
    *  @param firstName Database column FIRST_NAME SqlType(VARCHAR), Length(50,true)
    *  @param lastName Database column LAST_NAME SqlType(VARCHAR), Length(50,true)
    *  @param emailAddress Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(250,true)
    *  @param password Database column PASSWORD SqlType(VARCHAR), Length(250,true)
    *  @param registrationDate Database column REGISTRATION_DATE SqlType(DATE) */
  case class UserRow(id: String, firstName: String, lastName: String, emailAddress: String, password: String, registrationDate: java.sql.Date)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[String], e1: GR[java.sql.Date]): GR[UserRow] = GR{
    prs => import prs._
      UserRow.tupled((<<[String], <<[String], <<[String], <<[String], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "USER") {
    def * = (id, firstName, lastName, emailAddress, password, registrationDate) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(emailAddress), Rep.Some(password), Rep.Some(registrationDate)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val id: Rep[String] = column[String]("ID", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column FIRST_NAME SqlType(VARCHAR), Length(50,true) */
    val firstName: Rep[String] = column[String]("FIRST_NAME", O.Length(50,varying=true))
    /** Database column LAST_NAME SqlType(VARCHAR), Length(50,true) */
    val lastName: Rep[String] = column[String]("LAST_NAME", O.Length(50,varying=true))
    /** Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(250,true) */
    val emailAddress: Rep[String] = column[String]("EMAIL_ADDRESS", O.Length(250,varying=true))
    /** Database column PASSWORD SqlType(VARCHAR), Length(250,true) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(250,varying=true))
    /** Database column REGISTRATION_DATE SqlType(DATE) */
    val registrationDate: Rep[java.sql.Date] = column[java.sql.Date]("REGISTRATION_DATE")

    /** Uniqueness Index over (emailAddress) (database name EMAIL_ADDRESS) */
    val index1 = index("EMAIL_ADDRESS", emailAddress, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))

  /** Entity class storing rows of table UsState
    *  @param id Database column ID SqlType(VARCHAR), PrimaryKey, Length(2,true)
    *  @param description Database column DESCRIPTION SqlType(VARCHAR), Length(20,true), Default(None) */
  case class UsStateRow(id: String, description: Option[String] = None)
  /** GetResult implicit for fetching UsStateRow objects using plain SQL queries */
  implicit def GetResultUsStateRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[UsStateRow] = GR{
    prs => import prs._
      UsStateRow.tupled((<<[String], <<?[String]))
  }
  /** Table description of table US_STATE. Objects of this class serve as prototypes for rows in queries. */
  class UsState(_tableTag: Tag) extends Table[UsStateRow](_tableTag, "US_STATE") {
    def * = (id, description) <> (UsStateRow.tupled, UsStateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description).shaped.<>({r=>import r._; _1.map(_=> UsStateRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), PrimaryKey, Length(2,true) */
    val id: Rep[String] = column[String]("ID", O.PrimaryKey, O.Length(2,varying=true))
    /** Database column DESCRIPTION SqlType(VARCHAR), Length(20,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("DESCRIPTION", O.Length(20,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table UsState */
  lazy val UsState = new TableQuery(tag => new UsState(tag))
}
// $COVERAGE-ON$

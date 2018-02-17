package security.types

import security.authentication.Roles.ROLE.ROLE

case class User(username: String, expiryDate: String, role: ROLE)


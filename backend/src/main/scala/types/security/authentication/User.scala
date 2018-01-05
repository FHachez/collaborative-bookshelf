package types.security.authentication

import security.authentication.Roles.ROLE.ROLE

case class User(username: String, companyID: Long, expiryDate: String, role: ROLE)


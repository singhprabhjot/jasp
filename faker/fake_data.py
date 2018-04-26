import random
from faker import Faker
fake = Faker('en_US')

# first, import a similar Provider or use the default one
from faker.providers import BaseProvider

# create new provider class
class MyProvider(BaseProvider):
    def companyName(self):
        return 'Golden Sparrow'

    def isManager(self):
    	foo = ['true','false','false','false','false','false','false']
        return random.choice(foo)

# then add new provider to faker instance
fake.add_provider(MyProvider)


for _ in range(1000):
	print(
		"{"
		"name:\""+fake.first_name()+"\""
		", lastName:\""+fake.last_name()+"\""
		", companyName:\""+fake.companyName()+"\""
		", isManager:\""+fake.isManager()+"\""
		", SSN:\""+fake.ssn()+"\""
		", phone_number:\""+fake.phone_number()+"\""
		", city:\""+fake.city()+"\""
		", email:\""+fake.email()+"\""
		", zipcode:\""+fake.zipcode()+"\""
		", job:\""+fake.job()+"\""
		"}"
		)









# print(fake.companyName())
# print(fake.isManager())
# print(fake.ssn())
# print(fake.phone_number())
# print(fake.city())
# print(fake.email())
# print(fake.zipcode())
# print(fake.job())

# (Jaspreet:EMPLOYEE {name:'Jaspreet', Department:'Engineering', address:'Union City', Phone:'1234567890', Email:'jaspreet@abc.com', Date of Hire:'08/01/2010', Date of Birth:'28/12/1991',SSN:'123457367', Designation:'Manager'}),
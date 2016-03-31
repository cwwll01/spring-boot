/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.test.mock.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.MockSettings;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Annotation that can be used to add mocks to a Spring {@link ApplicationContext}. Can be
 * used as a class level annotation or on fields in either {@code @Configuration} classes,
 * or test classes that are {@link RunWith @RunWith} the {@link SpringRunner}.
 * <p>
 * Mocks can be registered by type or by {@link #name() bean name}. Any existing single
 * bean of the same type defined in the context will be replaced by the mock, if no
 * existing bean is defined a new one will be added.
 * <p>
 * When {@code @MockBean} is used on a field, as well as being registered in the
 * application context, the mock will also be injected into the field. Typical usage might
 * be: <pre class="code">
 * &#064;RunWith(SpringRunner.class)
 * public class ExampleTests {
 *
 *     &#064;MockBean
 *     private ExampleService service;
 *
 *     &#064;Autowired
 *     private UserOfService userOfService;
 *
 *     &#064;Test
 *     public void testUserOfService() {
 *         given(this.service.greet()).willReturn("Hello");
 *         String actual = this.userOfService.makeUse();
 *         assertEquals("Was: Hello", actual);
 *     }
 *
 *     &#064;Configuration
 *     &#064;Import(UserOfService.class) // A &#064;Component injected with ExampleService
 *     static class Config {
 *     }
 *
 *
 * }
 * </pre>
 * <p>
 * This annotation is {@code @Repeatable} and may be specified multiple times when working
 * with Java 8 or contained within an {@link MockBeans @MockBeans} annotation.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see MockitoPostProcessor
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(MockBeans.class)
public @interface MockBean {

	/**
	 * The name of the bean that should be registered with the application context. If not
	 * specified the name will either be generated or, if the mock replaces an existing
	 * bean, the existing name will be used.
	 * @return the name of the bean
	 */
	String name() default "";

	/**
	 * The classes to mock. This is an alias of {@link #classes()} which can be used for
	 * brevity if no other attributes are defined. See {@link #classes()} for details.
	 * @return the classes to mock
	 */
	@AliasFor("classes")
	Class<?>[] value() default {};

	/**
	 * The classes to mock. Each class specified here will result in a mock being created
	 * and registered with the application context. Classes can be omitted when the
	 * annotation is used on a field.
	 * <p>
	 * When {@code @MockBean} also defines a {@code name} this attribute can only contain
	 * a single value.
	 * <p>
	 * If this is the only attribute specified consider using the {@code value} alias
	 * instead.
	 * @return the classes to mock
	 */
	@AliasFor("value")
	Class<?>[] classes() default {};

	/**
	 * Any extra interfaces that should also be declared on the mock. See
	 * {@link MockSettings#extraInterfaces(Class...)} for details.
	 * @return any extra interfaces
	 */
	Class<?>[] extraInterfaces() default {};

	/**
	 * The {@link Answers} type to use on the mock.
	 * @return the answer type
	 */
	Answers answer() default Answers.RETURNS_DEFAULTS;

	/**
	 * If the generated mock is serializable. See {@link MockSettings#serializable()} for
	 * details.
	 * @return if the mock is serializable
	 */
	boolean serializable() default false;

	/**
	 * The reset mode to apply to the mock bean. The default is {@link MockReset#AFTER}
	 * meaning that mocks are automatically reset after each test method is invoked.
	 * @return the reset mode
	 */
	MockReset reset() default MockReset.AFTER;

}

/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

/**
 * @author ekleinod
 */
public class VectorGraphicsEncoderTest {

	@Test
	public void testAddFileExtension() {

		for (VectorGraphicsFormat format : VectorGraphicsFormat.values()) {

			// test -> test.svg
			Assert.assertEquals(String.format("test.%s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension("test", format));

			// test.svg -> test.svg
			Assert.assertEquals(String.format("test.%s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%s", format.toString().toLowerCase()), format));

			// test.svgsvg -> test.svgsvg.svg
			Assert.assertEquals(String.format("test.%1$s%1$s.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%1$s%1$s", format.toString().toLowerCase()), format));

			// test.svg.svg -> test.svg.svg
			Assert.assertEquals(String.format("test.%1$s.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%1$s.%1$s", format.toString().toLowerCase()), format));

			// test.txt -> test.txt.svg
			Assert.assertEquals(String.format("test.txt.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension("test.txt", format));

		}

	}

}
